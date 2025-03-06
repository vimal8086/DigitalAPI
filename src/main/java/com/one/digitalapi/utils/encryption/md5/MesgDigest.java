/** 
 * Java file: MesgDigest.java
 * @version Apr 25 1997
 * @author Russell Quong
 */

package com.one.digitalapi.utils.encryption.md5;

/**
 * <tt>class <font color=green>MesgDigest</font></tt>:
 * An implementation of MesgDigest takes input data <em>IX</em> 
 * and generates a short, fixed-length 
 * <font color=#339933>message-digest</font> or 
 * <font color=#339933>fingerprint</font> of the input.
 * 
 * <p> Although it is possible two humans would have the same fingerprint, 
 * it is highly unlikely.  
 * Similar, although it is possible two sources of input data
 * have the same fingerprint, it is highly unlikely.  In particular,
 * 
 * <ul>
 * <li> Given a message-digest value MD, it is (supposed to be) 
 * computationally difficult to 
 * find  <strong><font color=993333>any</font></strong> input sequence 
 * <i>IX'</i> that generates MD.
 * 
 * <li> Or equivalently, 
 * given original data IX and its corresponding message-digest MD,
 * it is difficult for an intruder to
 * corrupt the original data, yet still retain the original message-digest, 
 * as the intruder would have to find <i>IX'</i>.
 * <ul>
 * 
 * <p>For example, suppose you are downloading a program from an 
 * untrusted mirror site, but the original author posted a fingerprint
 * of her original program at her (trusted) site.  (There is little need
 * to mirror the fingerprint as it is small.)
 * You can easily verify you got an unaltered version by calculating its
 * fingerprint.
 * 
 * <p> The popular MD4/MD5 algorithms generate a 128 bit (16 byte) 
 * fingerprint.  
 * (Note, the MD4 algorithm is now considered insecure, and should not
 *  be used in the future.)
 * The SHA-1 (secure hash algorithm, a NIST standard) generates a 160 bit 
 * fingerprint.  
 * (I have not implemented SHA-1 as 5/13/97; anyone interested?)
 * 
 * <p> Sadly (for me), this functionality is now available in the 
 * JDK 1.1 in the java.security.MessageDigest abstract class.
 * 
 * @version Apr 16 1997, Version 1.0
 * @author Russell Quong
 * @org    VA Research
 */
public interface MesgDigest {
    public void init ();    
    public void addInput (String s);
    public void addInput (byte [] input, int inputLen);
    public byte [] getMD ();
    public int digestLen ();		// number of bytes
} /* MesgDigest */

    
