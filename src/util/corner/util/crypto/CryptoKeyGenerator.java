//==============================================================================
// file :       SimitriKeyGenerator.java
// project:     corner-utils
//
// last change: date:       $Date: 2005-06-15 17:08:39 +0800 $
//              by:         $Author: jcai $
//              revision:   $Revision: 39 $
//------------------------------------------------------------------------------
//copyright:	China Java Users Group http://cnjug.dev.java.net
//License:      the Apache License, Version 2.0 (the "License")
//==============================================================================
package corner.util.crypto;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.security.NoSuchAlgorithmException;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

/**
 * �����㷨��key������.
 * @author <a href="http://wiki.java.net/bin/view/People/JunTsai">Jun Tsai</a>
 * @version $Revision: 39 $
 */
public class CryptoKeyGenerator {
	/**
	 * �������ܵ�key.
	 * @param algorithm �㷨����.
	 * @param fileName ���ܲ����ļ�������.
	 */
	public static void generateKey(String algorithm, String fileName) {
		SecretKey key = null;

		try {

			//	  ָ���㷨,����ΪDES;�������Blowfish�㷨,���� getInstance("Blowfish")
			//	  BouncyCastle������֧������ͨ�ñ�׼�㷨
			KeyGenerator keygen = KeyGenerator.getInstance(algorithm);
			//	  ָ����Կ����,����Խ��,����ǿ��Խ��
			keygen.init(56);
			//	  ������Կ
			key = keygen.generateKey();
			//	  ��������ļ�,�����Ŀ¼�Ƕ�̬��,�����û�����������Ŀ¼
			ObjectOutputStream keyFile =
				new ObjectOutputStream(new FileOutputStream(fileName));
			keyFile.writeObject(key);
			keyFile.close();
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

	}
}
