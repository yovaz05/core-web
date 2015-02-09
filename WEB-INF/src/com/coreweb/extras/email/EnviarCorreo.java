package com.coreweb.extras.email;


import java.security.GeneralSecurityException;
import java.util.Properties;  

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;  
import javax.mail.MessagingException;  
import javax.mail.PasswordAuthentication;  
import javax.mail.Session;  
import javax.mail.Transport;  
import javax.mail.internet.InternetAddress;  
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;  
import javax.mail.internet.MimeMultipart;

import com.coreweb.SistemaPropiedad;


  
  
public class EnviarCorreo{  
/*
 *  OJO ESTOS DATOS AHORA HAY QUE CARGARLOS DESDE EL sistema-propiedad.ini
 * 
    private static final String SMTP_HOST_NAME = "smtp.gmail.com";  
    private static final String SMTP_PORT = "587";  
    private static final String SMTP_START_TLS_ENABLE = "true";
    private static final String EMAIL_FROM = "yhaguyrepuestos@gmail.com";
    private static final String EMAIL_FROM_PASSWORD = "yrmkt1970";

	// ======================

	private static String SMTP_HOST_NAME = "mail.vidrioluz.com.py";  
    private static String SMTP_PORT = "2525";  
    private static String SMTP_START_TLS_ENABLE = "false";

    private static String EMAIL_FROM = "daniel@vidrioluz.com.py";
    private static String EMAIL_FROM_PASSWORD = "qwerty";

*/
    
    
 //   private static final String xxxSSL_FACTORY = "javax.net.ssl.SSLSocketFactory";  
 
    SistemaPropiedad sisPro = new SistemaPropiedad();
    

    public void sendMessage(String recipients[], String[] recipientsCCO, String subject,  
            String message) throws Exception {  
    	    	
    	sendMessage(recipients, new String[]{""}, recipientsCCO, subject,  
                message, this.sisPro.getEmailDefault(), this.sisPro.getEmailPassDefault(), null, null) ;
    }

    
    
    public void sendMessage(String[] recipients, String[] recipientsCC, String[] recipientsCCO, String subject,  
            String message, String from, String pass, String fileName, String path) throws Exception {  
    	
    	if (this.sisPro.siEnviarCorreo() == false){
    		return;
    	}
    	
        boolean debug = false;         
        Properties props = new Properties();  

   
		props.put("mail.smtp.host", this.sisPro.getSmtpHost());
		props.put("mail.smtp.port", this.sisPro.getSmtpPort());
		props.put("mail.smtp.starttls.enable", this.sisPro.getSmtpStatTlsEnable());
		props.put("mail.smtp.auth", "true");

		/*
        Session session = Session.getDefaultInstance(props,  
                new javax.mail.Authenticator() {  
                    protected PasswordAuthentication getPasswordAuthentication() {  
                        return new PasswordAuthentication(from,  
                                emailFromPasswrd);  
                    }  
                });  
        */

        
        Session session = Session.getDefaultInstance(props, new MiAuthenticator(from, pass)); 
        
  
        session.setDebug(debug);  

    	BodyPart mensaje = new MimeBodyPart();
    	mensaje.setText(message);
    	
    	MimeMultipart multiParte = new MimeMultipart();
    	multiParte.addBodyPart(mensaje);

        if (path != null){
            BodyPart adjunto = new MimeBodyPart();
        	adjunto.setDataHandler(new DataHandler(new FileDataSource(path)));
        	adjunto.setFileName(fileName);
        	multiParte.addBodyPart(adjunto);
        }
    	
  
        Message msg = new MimeMessage(session);
        //aqui cambiar la cuenta, para que reciba como parametro..provisoriamente usa esta cuenta
        InternetAddress addressFrom = new InternetAddress(from);  
        msg.setFrom(addressFrom);
          
        InternetAddress[] addressTo = new InternetAddress[recipients.length];  
        for (int i = 0; i < recipients.length; i++) {  
            addressTo[i] = new InternetAddress(recipients[i]);  
        }  
        msg.setRecipients(Message.RecipientType.TO, addressTo);
        
        //verifica si destinatario CC no es vacio
        if (recipientsCC[0].isEmpty()==false) {
        	
        	InternetAddress[] addressToCC = new InternetAddress[recipientsCC.length];  
            for (int i = 0; i < recipientsCC.length; i++) {  
                addressToCC[i] = new InternetAddress(recipientsCC[i]);  
            }  
            msg.setRecipients(Message.RecipientType.CC, addressToCC);
            
		}
        
      //verifica si destinatario CCO no es vacio
        if (recipientsCCO[0].isEmpty()==false) {
        	
        	InternetAddress[] addressToCCO = new InternetAddress[recipientsCCO.length];  
            for (int i = 0; i < recipientsCCO.length; i++) {  
                addressToCCO[i] = new InternetAddress(recipientsCCO[i]);  
            }  
            msg.setRecipients(Message.RecipientType.BCC, addressToCCO);
            
		}
        
  
        //Configura el asunto y el tipo de contenido 
        msg.setSubject(subject);
        msg.setContent(multiParte);        
        
        Transport.send(msg);
        
    }
    
    
    public static void main(String[] args) {
		try {
			
			SistemaPropiedad.reloadSistemaPropiedad("/home/daniel/datos/varios/propuestas/scg33/proyecto-scg33/scg33/WEB-INF/sistema-propiedad.ini");
			
			System.out.println("==== paso =====");
			EnviarCorreo co = new EnviarCorreo();
			String[] recipients = {"daniel.omar.romero@gmail.com"};
			String[] recipientsCC = {""};
			String[] recipientsCCO = {""};
			String subject = "asunto va";
			String message = "mensaje";
			String from = "daniel@vidrioluz.com.py";
			String pass = "qwerty";
			String fileName = null;
			String path = null;
			
			co.sendMessage(recipients, recipientsCC, recipientsCCO, subject, message, from, pass, fileName, path);
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}


class MiAuthenticator extends javax.mail.Authenticator {
	
	String from = "desde";
	String pass = "clave";
	
	public MiAuthenticator(String from, String pass){
		this.from = from;
		this.pass = pass;
		
	}
	
	protected PasswordAuthentication getPasswordAuthentication() {  
        return new PasswordAuthentication(this.from,this.pass);  
    }  
}





