//==============================================================================
// file :       Crypter.java
// project:     corner-utils
//
// last change: date:       $Date: 2005-07-01 13:35:31 +0800 $
//              by:         $Author: jcai $
//              revision:   $Revision: 131 $
//------------------------------------------------------------------------------
//copyright:	China Java Users Group http://cnjug.dev.java.net
//License:      the Apache License, Version 2.0 (the "License")
//==============================================================================
package corner.util.crypto;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * ���ļ�����.
 * @author <a href="http://wiki.java.net/bin/view/People/JunTsai">Jun Tsai</a>
 * @version $Revision: 131 $
 */
public class Cryptor {
	private static final Log log = LogFactory.getLog(Cryptor.class);
	/**
	 * ����д���ļ���IO��,��IO�������˼��ܴ���,ʹ����������ļ�.
	 * @param outFileName ������ļ�����. 
	 * @param keyFile ��Կ���ļ�����.
	 * @return ���ܺ�������.
	 */
	public static OutputStream encryptFileIO(
		String outFileName,
		String keyFile) {
		if(keyFile==null){
			try {
				return new FileOutputStream(outFileName);
			} catch (FileNotFoundException e) {
				throw new RuntimeException(e);
			}
		}
		SecretKey key = null;

		//����Կ�ļ���ȡ��Կ
		ObjectInputStream keyis;
		try {
			keyis = new ObjectInputStream(new FileInputStream(keyFile));
			key = (SecretKey) keyis.readObject();
			keyis.close();
		} catch (FileNotFoundException e) {
			log.error("file not found!", e);
			throw new RuntimeException("file not found", e);
		} catch (IOException e) {
			log.error("io occour exception", e);
			throw new RuntimeException(e);
		} catch (ClassNotFoundException e) {
			log.error("Class Not Found  exception", e);
			throw new RuntimeException(e);
		}

		//��key����Cipher
		Cipher cipher = null;
		//����Ҫ��Cipher��ʵ��

		try {
			cipher = Cipher.getInstance("DES");
			//���ü���ģʽ

			cipher.init(Cipher.ENCRYPT_MODE, key);
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		} catch (NoSuchPaddingException e) {
			throw new RuntimeException(e);
		} catch (InvalidKeyException e) {
			throw new RuntimeException(e);
		}

		//�ӶԻ�����ȡ��Ҫ���ܵ��ļ�

		//���벢�����ļ�
		CipherOutputStream out = null;
		try {
			out =
				new CipherOutputStream(
					new BufferedOutputStream(new FileOutputStream(outFileName)),
					cipher);
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		}

		return out;

	}
	/**
	 * ���ܶ�ȡ�ļ���IO��.��IO�����н���,ʹ������������ļ�.
	 * @param inputFileName ��Ҫ��ȡ���ļ�����.
	 * @param keyFile ��Կ�ļ�������.
	 * @return ���ܺ��IO��.
	 */
	public static InputStream dencryptFileIO(
		String inputFileName,
		String keyFile) {
			if(keyFile==null){
				try {
					return new FileInputStream(new File(inputFileName));
				} catch (FileNotFoundException e) {
					throw new RuntimeException(e);
				}
			}
		SecretKey key = null;

		ObjectInputStream keyis;
		try {
			keyis = new ObjectInputStream(new FileInputStream(keyFile));
			key = (SecretKey) keyis.readObject();

			keyis.close();
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}

		//��key����Cipher
		Cipher cipher = null;

		try {
			//�����㷨,Ӧ�������ʱ������һ��
			cipher = Cipher.getInstance("DES");
			// ���ý���ģʽ
			cipher.init(Cipher.DECRYPT_MODE, key);
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		} catch (NoSuchPaddingException e) {
			throw new RuntimeException(e);
		} catch (InvalidKeyException e) {
			throw new RuntimeException(e);
		}

		File file = new File(inputFileName);

		try {

			//������
			CipherInputStream in =
				new CipherInputStream(
					new BufferedInputStream(new FileInputStream(file)),
					cipher);
			return in;

		} catch (Exception e) {
			throw new RuntimeException(e);

		}

	}
}
