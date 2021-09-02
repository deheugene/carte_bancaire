package carte;

import javacard.framework.*;

/**
 * 
 * @author DEH EUGENE
 */
public class Carte extends Applet {

	/* Constantes */
	public static final byte INS_SELECT= (byte) 0xA4;
	public static final byte CLA_ISO7816= 0x00;
	public static final byte CLA_MONAPPLET = (byte) 0xB0;
	public static final byte INS_CREDITER_COMPTE = 0x10;
	public static final byte INS_DEBITER_COMPTE = 0x20;
	public static final byte INS_INTERROGER_SOLDE = 0x30;
	public static final byte INS_INTERROGER_NUM_COMPTE = 0x40;
	public static final byte INS_INTERROGER_NOM = 0x50;
	public static final byte INS_INTERROGER_PRENOM = 0x60;
	public static final byte INS_VERIFIER_CODE = 0x70;
	final static byte INS_INIT_SOLDE =0x09;
	final static byte INS_BLOC_COMPTE =0x07;
	final static  byte INS_DEBLOC_COMPTE = 0x06;
	final static byte MaxEssai_PIN = (byte)0x03;
	final static byte MaxLg_PIN = (byte)0x08;
	final static short SoldeNegatif_SW = (short)0x6910;
	
	/* Attributs */
	/*
	private byte[] numCompte={0x01,0x02,0x0c,0x07,0x05,0x06,0x08};
    private byte[] nom={0x01,0x0d,0x00,0x00,0x09,0x0b,0x04};
	private byte[] prenom={0x01,0x02,0x0d,0x01,0x05,0x0a,0x09};
	*/
	private byte[] numCompte= {10,50,53,52,54,50,51,52,57,55};
    private byte[] nom={87,73,76,73,68,65};
	private byte[] prenom={74,65,67,81,85,69,76,73,78,69};
	private byte solde[]={50,48,50,56,50,56,56,48};
	public static void install(byte[] bArray, short bOffset, byte bLength) {
		new Carte(bArray, bOffset, bLength);
	}

	public void process(APDU apdu) throws ISOException {
		byte[] buffer = apdu.getBuffer();

		if (this.selectingApplet()) {
			return;
		}

		if (buffer[ISO7816.OFFSET_CLA] != CLA_MONAPPLET) {
			ISOException.throwIt(ISO7816.SW_CLA_NOT_SUPPORTED);
		}

		switch (buffer[ISO7816.OFFSET_INS]) {
		case INS_CREDITER_COMPTE:
			apdu.setIncomingAndReceive();
			solde=new byte[(byte)apdu.getIncomingLength()];
			Util.arrayCopy(buffer, ISO7816.OFFSET_CDATA, solde,(byte) 0, (byte)apdu.getIncomingLength());
			Util.arrayCopy(solde, (byte)0, buffer, ISO7816.OFFSET_CDATA, (byte)solde.length);
			apdu.setOutgoingAndSend(ISO7816.OFFSET_CDATA, (byte)solde.length);
			break;

		case INS_DEBITER_COMPTE:
			apdu.setIncomingAndReceive();
			solde=new byte[(byte)apdu.getIncomingLength()];
			Util.arrayCopy(buffer, ISO7816.OFFSET_CDATA, solde,(byte) 0, (byte)apdu.getIncomingLength());
			Util.arrayCopy(solde, (byte)0, buffer, ISO7816.OFFSET_CDATA, (byte)solde.length);
			apdu.setOutgoingAndSend(ISO7816.OFFSET_CDATA, (byte)solde.length);
			break;

		case INS_INTERROGER_SOLDE:
			Util.arrayCopy(solde, (byte)0, buffer, ISO7816.OFFSET_CDATA, (byte)solde.length);
			apdu.setOutgoingAndSend(ISO7816.OFFSET_CDATA, (byte)solde.length);
			break;

		case INS_INTERROGER_NUM_COMPTE:
			Util.arrayCopy(numCompte, (byte)0, buffer, ISO7816.OFFSET_CDATA, (byte)numCompte.length);
			apdu.setOutgoingAndSend(ISO7816.OFFSET_CDATA, (byte)numCompte.length);
			break;
		case INS_INTERROGER_NOM:
			// Copie du contenu du message dans le buffer de la reponse 
            Util.arrayCopy(nom, (byte)0, buffer, ISO7816.OFFSET_CDATA, (byte)nom.length);
            // Envoi de la réponse
            apdu.setOutgoingAndSend(ISO7816.OFFSET_CDATA, (byte)nom.length);
			break;
		case INS_INTERROGER_PRENOM:
			// Copie du contenu du message dans le buffer de la reponse 
            Util.arrayCopy(prenom, (byte)0, buffer, ISO7816.OFFSET_CDATA, (byte)prenom.length);
            // Envoi de la réponse
            apdu.setOutgoingAndSend(ISO7816.OFFSET_CDATA, (byte)prenom.length);
			break;
		case INS_INIT_SOLDE:
			apdu.setIncomingAndReceive();
			solde=new byte[(byte)apdu.getIncomingLength()];
			Util.arrayCopy(buffer, ISO7816.OFFSET_CDATA, solde,(byte) 0, (byte)apdu.getIncomingLength());
			Util.arrayCopy(solde, (byte)0, buffer, ISO7816.OFFSET_CDATA, (byte)solde.length);
			apdu.setOutgoingAndSend(ISO7816.OFFSET_CDATA, (byte)solde.length);
			break;
		case INS_BLOC_COMPTE:
			apdu.setOutgoingAndSend((short) 0, (byte)1);
			break;
		case INS_DEBLOC_COMPTE:
			apdu.setOutgoingAndSend((short) 0, (byte)1);
			break;

		default:
			ISOException.throwIt(ISO7816.SW_INS_NOT_SUPPORTED);
		}
	}

	protected Carte(byte[] bArray, short bOffset, byte bLength) {
		register();
	}

	public boolean select() {
		return true;
	}

	public void deselect() {
	}
}
