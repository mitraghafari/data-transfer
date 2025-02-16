package com.np.ifcp.repair.common;

import javax.crypto.*;
import javax.crypto.spec.DESKeySpec;
import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class DES {

    public static String createMac(String key, Object... objects) {
        if (key.equals(""))
            return "";
        StringBuilder sb = new StringBuilder();
        for (Object object : objects)
            if (object == null)
                sb.append("$");
            else
                sb.append(object.toString()).append("$");

        ByteArrayInputStream bais = new ByteArrayInputStream(sb.toString().getBytes());
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            encryptOrDecrypt(key, Cipher.ENCRYPT_MODE, bais, baos);
            return sha1(baos.toByteArray());
        } catch (Throwable e) {
            e.printStackTrace();
            return "";
        }
    }

    public static boolean checkMac(String key, String mac, Object... objects) {
        System.out.println(createMac(key, objects));
        return mac.equals(createMac(key, objects));
    }

//	public static void main(String[] args) {
//		String key = "squirrel12lkjfsdljf;lsdajf;ldkjsa3fdsfsv"; // needs to be at least 8 characters for DES
//		String data = "salama;lkfjdsa;lkjfdsa;lkjfdsa;lkjfd;lsakjf;lsdkajdfsa;lkjdsalama;lkfjdsa;lkjfdsa;lkjfdsa;lkjfd;lsakjf;lsdkajdfsa;lkjdsalama;lkfjdsa;lkjfdsa;lkjfdsa;lkjfd;lsakjf;lsdkajdfsa;lkjddsfdfadasfdsafsdafsdasa";
//		Object data2[] = { "salam", new Integer(2), null, "alo" };
//		long l1 = System.currentTimeMillis();
//		// for (int i = 0; i < 100000; i++)
//		checkMac(key, "72929aee2c07ff425fe6cbea52c8ec8dfca1612d", data2);
//		System.out.println(System.currentTimeMillis() - l1);
//		System.out.println(createMac(key, data));
//		System.out.println(checkMac(key, "72929aee2c07ff425fe6cbea52c8ec8dfca1612d", data));
//	}

    private static void encryptOrDecrypt(String key, int mode, InputStream is, OutputStream os) throws Throwable {

        DESKeySpec dks = new DESKeySpec(key.getBytes());
        SecretKeyFactory skf = SecretKeyFactory.getInstance("DES");
        SecretKey desKey = skf.generateSecret(dks);
        Cipher cipher = Cipher.getInstance("DES"); // DES/ECB/PKCS5Padding for SunJCE

        if (mode == Cipher.ENCRYPT_MODE) {
            cipher.init(Cipher.ENCRYPT_MODE, desKey);
            CipherInputStream cis = new CipherInputStream(is, cipher);
            doCopy(cis, os);
        } else if (mode == Cipher.DECRYPT_MODE) {
            cipher.init(Cipher.DECRYPT_MODE, desKey);
            CipherOutputStream cos = new CipherOutputStream(os, cipher);
            doCopy(is, cos);
        }
    }

    private static void doCopy(InputStream is, OutputStream os) throws IOException {
        byte[] bytes = new byte[64];
        int numBytes;
        while ((numBytes = is.read(bytes)) != -1) {
            os.write(bytes, 0, numBytes);
        }
        os.flush();
        os.close();
        is.close();
    }

    private static String sha1(byte[] input) throws NoSuchAlgorithmException {
        MessageDigest mDigest = MessageDigest.getInstance("SHA1");
        byte[] result = mDigest.digest(input);
        StringBuilder sb = new StringBuilder();
        for (byte b : result) {
            sb.append(Integer.toString((b & 0xff) + 0x100, 16).substring(1));
        }

        return sb.toString();
    }
}
