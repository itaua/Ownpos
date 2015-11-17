package itauamachado.ownpos.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;

import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.StyleSpan;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import itauamachado.ownpos.MapaActivity;
import itauamachado.ownpos.R;
import itauamachado.ownpos.domain.SQLiteConn;
import itauamachado.ownpos.domain.WrapObjToNetwork;
import itauamachado.ownpos.domain.objAcervo;
import itauamachado.ownpos.extras.Util;
import itauamachado.ownpos.network.NetworkConnection;
import itauamachado.ownpos.network.Transaction;

public class BReceiver_Context extends BroadcastReceiver implements  Transaction {

	public static final String ACERVO = BReceiver_Context.class.getName()+"acervo";
	public static final String AULA = BReceiver_Context.class.getName()+"aula";
	public static final String COMPROMISSO = BReceiver_Context.class.getName()+"compromisso";
	private boolean nAcervo, nAula, nCompromisso = false;

	private Context mContext;
	private Intent mIntent;
	private List<ScanResult> wifiList;


	@Override
	public void onReceive(Context context, Intent intent) {
		mContext = context;
		mIntent = intent;
		wifiList = new ArrayList<>();
		Util.log("onReceive WIFI - BUSCANDO");

		WifiManager mMainWifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		boolean originalState = mMainWifi.isWifiEnabled();
		if (originalState) {

			Util.log("originalState true");
			mMainWifi.startScan();
			wifiList = mMainWifi.getScanResults();
		}

		if(wifiList.size() > 0){
			for(int i =0; i < wifiList.size(); i++) {


				//if(wifiList.get(i).SSID.equalsIgnoreCase(Util.REDE_SENAC)){
				if(true){

					hasNotification(); //saber se já foi notificado hoje

					if(!nAcervo){	//saber se tem acervo
						Util.log("Buscando acervo, usuário dentro do SENAC");
						List<objAcervo> objAcervoList = new SQLiteConn(mContext).getReserva();
						if (objAcervoList.size() > 0) {
							for (int j = 0; j < objAcervoList.size(); j++) {
								callVolleyRequest(ACERVO, String.valueOf(objAcervoList.get(j).get_senac()));
							}
						}
						new SQLiteConn(mContext).setNotification(ACERVO);
					}
				}

				if (!nAula) {
					//saber se tem aula

				}

				if (!nCompromisso) {
					//saber se tem compromisso

				}
			}
		}
		//gerarNotificacao(context, new Intent(context, MainActivity.class), "Nova mensagem", "Título", "Descrição nova mensagem");
	}

	public void hasNotification(){
		List<String> list = new SQLiteConn(mContext).getNotifications();
		Util.log("Tamanho da lista: " + list.size());

        for (int i = 0; i < list.size(); i++){
			Util.log("Tamanho da lista: "+list.size() + " Valor:"+list.get(i));
			if(list.get(i).equalsIgnoreCase(ACERVO)){
				nAcervo = true;
			}else
			if(list.get(i).equalsIgnoreCase(AULA)){
				nAula = true;
			}else
			if(list.get(i).equalsIgnoreCase(COMPROMISSO)){
				nCompromisso = true;
			}
		}
	}

	public void callVolleyRequest(String tipo, String valor){
		Util.log("Tipo: "+tipo+" Valor"+valor);
		NetworkConnection.getInstance(mContext).execute(this, tipo, valor);
	}

	@Override
	public WrapObjToNetwork doBefore() {
		return null;
	}

	@Override
	public void doAfter(JSONArray jsonArray) {

		if( jsonArray != null ){
			try {
				String tipo = "";
				List<JSONObject> list = new ArrayList<>();
				for (int i = 0; i < jsonArray.length(); i++) {
					//Util.log(jsonArray.getJSONObject(i).toString());

					if(jsonArray.getJSONObject(i).has("notification")){
						tipo = jsonArray.getJSONObject(i).getString("notification");
					}
					list.add(jsonArray.getJSONObject(i));
				}

				if(tipo.equalsIgnoreCase(ACERVO)){
					//Util.log("Preparar Notificação Acervo: (Lista: "+list.size()+")");
					preparaNotificationAcervo(list);
				}else if(tipo.equalsIgnoreCase(AULA)){
					preparaNotificationAula(list);
					Util.log("preparaNotificationAula");
				}else if (tipo.equalsIgnoreCase(COMPROMISSO)){
					preparaNotificationCompromissos(list);
					Util.log("preparaNotificationCompromissos");
				}
			}
			catch(JSONException e){
				Util.log("doAfter(): " + e.getMessage());
			}
		}
	}

	public void preparaNotificationAcervo(List<JSONObject> dados){
		Bundle bundle = new Bundle();
		Intent intent = new Intent(mContext, MapaActivity.class);
		ContentValues contentValues = new ContentValues();
		List<ContentValues> acervoList = new ArrayList<>();
		for (int i = 0; i< dados.size(); i++){
			//Util.log("Registro "+i+" = "+dados.get(i).toString());
			for(int j = 0; j < dados.get(i).length(); j++){
				if(dados.get(i).has("notification")){
					try{
						//Util.log("Registro "+i+" = "+dados.get(i).toString());
						if(!(contentValues.containsKey(dados.get(i).getString("_senac")))){
							contentValues.put( "acervo", dados.get(i).getString("_senac"));
						}

					}catch (Exception e){
						e.printStackTrace();
					}

				}else{
					try {
						contentValues.put( "exemplar", dados.get(i).getString("exemplar"));
						contentValues.put( "d"+dados.get(i).getString("exemplar"), dados.get(i).getString("disponib"));
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
			acervoList.add(contentValues);
			contentValues = new ContentValues();
		}

		// [acervo=6456]
		// [9281380 =Consulta local , acervo=29838]
		String titulo = "Disponibilidade de acervo";
		String mensagem= "";
		String acervo;
		if(acervoList.size()!= 0){
			for (ContentValues c: acervoList) {
				//Util.log(c.toString());
				if(c.containsKey("exemplar")){
					String key = c.getAsString("exemplar");
					if(c.getAsString("d"+key).equalsIgnoreCase("Dispon\u00edvel\u00a0")){
						mensagem = "Existem livros disponíveis na biblioteca.";
						break;
					}else
					if(c.getAsString("d"+key).equalsIgnoreCase("Consulta local\u00a0")){
						mensagem = "Existem livros para consulta local";
					}else{
						mensagem = "";
					}
				}
			}
		}

		if(mensagem.trim().length()!=0){
			bundle.putString("title", titulo);
			bundle.putString("mensagem", mensagem);
			bundle.putString(Util.METHOD, Util.NAVEGACAO);
			bundle.putInt("pontoB", 136);

			TaskStackBuilder stack = TaskStackBuilder.create(mContext);
			stack.addParentStack(MapaActivity.class);
			intent.putExtras(bundle);
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
			stack.addNextIntent(intent);

			PendingIntent pi = stack.getPendingIntent( 0, PendingIntent.FLAG_UPDATE_CURRENT );
			setNotificationApp(bundle, pi);
		}else{
			Util.log("Sem disponibilidade de acervo");
		}

	}

	public void preparaNotificationAula(List<JSONObject> dados){
		Bundle bundle = new Bundle();
		bundle.putString("title","titulo" );
		bundle.putString("mensagem", "mensagem obtendo a maior quantidade de informação posssivel sobre a notificação, pois esta é uma mensagem bigtex");
		setNotificationApp(null,null);
	}

	public void preparaNotificationCompromissos(List<JSONObject> dados){
		Bundle bundle = new Bundle();
		bundle.putString("title","titulo" );
		bundle.putString("mensagem", "mensagem obtendo a maior quantidade de informação posssivel sobre a notificação, pois esta é uma mensagem bigtex");
		setNotificationApp(null, null);
	}

	public void gerarNotificacao(Context context, Intent intent, CharSequence ticker, CharSequence titulo, CharSequence descricao){
		NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		PendingIntent p = PendingIntent.getActivity(context, 0, intent, 0);

		NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
		builder.setTicker(ticker);
		builder.setContentTitle(titulo);
		builder.setContentText(descricao);
		builder.setSmallIcon(R.drawable.ownpos_icon);
		builder.setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.ownpos_icon));
		builder.setContentIntent(p);

		Notification n = builder.build();
		n.vibrate = new long[]{150, 300, 150, 600};
		n.flags = Notification.FLAG_AUTO_CANCEL;
		nm.notify(R.drawable.ownpos_icon, n);

		try{
			Uri som = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
			Ringtone toque = RingtoneManager.getRingtone(context, som);
			toque.play();
		}
		catch(Exception e){
			Util.log("gerarNotificacao() Exception: "+e.toString());
		}
	}

	private void setNotificationApp(Bundle data, PendingIntent pi ){
		final int id = 6565;

		final NotificationCompat.Builder builder = new NotificationCompat.Builder(mContext)
				.setTicker( data.getString("title") )
				.setSmallIcon( R.drawable.ownpos_icon )
				.setContentTitle( data.getString("title") )
				.setAutoCancel(true);
		builder.setContentIntent(pi);

		// BIG CONTENT
			NotificationCompat.BigTextStyle bigText = new NotificationCompat.BigTextStyle();
			bigText.bigText(data.getString("mensagem"));
			builder.setStyle(bigText);

		//varias notificações ao mesmo tempo
		// INPUT STYLE

		if(false){
			NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
			inboxStyle.setSummaryText("ACERVO");
			int number = 3;

			for( int i = 0, tamI = 3; i < tamI; i++ ){

				String messsage = "mensagem "+i+" : descrição da mensagem" ;
				int size = messsage.indexOf(":");
				Spannable sb = new SpannableString( messsage );
				sb.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 0, size, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
				inboxStyle.addLine(sb);
			}
			inboxStyle.setBigContentTitle( number + " novas mensagens" );
			builder.setNumber(number);
			builder.setStyle(inboxStyle);

			builder.setVisibility(NotificationCompat.VISIBILITY_PUBLIC);
		}

		NotificationManager nm = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
		nm.notify(id, builder.build());
	}

}
