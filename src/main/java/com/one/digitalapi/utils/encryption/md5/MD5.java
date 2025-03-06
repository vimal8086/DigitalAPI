/** 
 * Java file: MD5.java
 * @version Apr 16 1997
 * @author Russell Quong
 * @org    VA Research
 */

/**
 * Implements the MD5 digital-signature/message-digest algorithm, RFC 1321.<br>
 *    RSA Data Security, Inc. MD5 Message-Digest Algorithm<br>
 * 
 * This RFC is available at many, many places, including
 * <a href="http://www.neda.com/rfc/"> www.neda.com </a>
 * 
 * Here is the official RSA disclaimer:
 * <pre>
 * License to copy and use this software is granted provided that it
 * is identified as the "RSA Data Security, Inc. MD5 Message-Digest
 * Algorithm" in all material mentioning or referencing this software
 * or this function.
 * 
 * License is also granted to make and use derivative works provided
 * that such works are identified as "derived from the RSA Data
 * Security, Inc. MD5 Message-Digest Algorithm" in all material
 * mentioning or referencing the derived work.
 * 
 * RSA Data Security, Inc. makes no representations concerning either
 * the merchantability of this software or the suitability of this
 * software for any particular purpose. It is provided "as is"
 * without express or implied warranty of any kind.
 * </pre>
 */

/*
 * Here is my own disclaimer.
 * 
 * Copyright (c) 1997      Russell W Quong.
 *
 * In the following, the "author" refers to "Russell Quong."
 *
 * Permission to use, copy, modify, distribute, and sell this software and
 * its documentation for any purpose is hereby granted without fee, provided
 * that the following conditions are met:
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 * 2. All advertising materials mentioning features or use of this software
 *    must display the following acknowledgement:
 *      This product includes software developed by Russell Quong.
 *
 * Any or all of these provisions can be waived if you have specific,
 * prior permission from the author.
 *
 * THE SOFTWARE IS PROVIDED "AS-IS" AND WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS, IMPLIED OR OTHERWISE, INCLUDING WITHOUT LIMITATION, ANY
 * WARRANTY OF MERCHANTABILITY OR FITNESS FOR A PARTICULAR PURPOSE.
 *
 * IN NO EVENT SHALL RUSSELL QUONG BE LIABLE FOR ANY SPECIAL,
 * INCIDENTAL, INDIRECT OR CONSEQUENTIAL DAMAGES OF ANY KIND, OR ANY
 * DAMAGES WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS,
 * WHETHER OR NOT ADVISED OF THE POSSIBILITY OF DAMAGE, AND ON ANY
 * THEORY OF LIABILITY, ARISING OUT OF OR IN CONNECTION WITH THE USE OR
 * PERFORMANCE OF THIS SOFTWARE.
 */

package com.one.digitalapi.utils.encryption.md5;

import java.io.FileInputStream;
import java.io.IOException;

import com.one.digitalapi.logger.ConsoleLogger;



/**
 * <tt>class <font color=green>MD5</font></tt>:
 * <p>Generate the 128-bit/16-byte MD5 
 * <font color=#006633><em>fingerprint</em></font>
 * or <font color=#006633><em>message digest</em></font>
 * for an arbitrary array of input bytes.  (Works under JDK 1.0.2)
 * I'll use the abbreviation <em>fp</em> for fingerprint.
 * 
 * <p>For example, to generate the MD5 fingerprint of a string SSS:
 * <pre>
 *     import qpl.util.*;		// current package, as distributed
 *     ...
 *     MD5 md = new MD5();		
 *     String sss = ...;
 *     md.addInput(sss);		// Input data to process
 *     byte [] fp = md.getMD();	// get the MD5 fingerprint.
 * 
 *     System.out.println( md.bytes2hexStr(fp) );	// print out in hex
 * </pre>
 * 
 * <p>If you read data in chunks, call <tt>addInput(chunk)</tt> 
 * after reading each chunk of data.
 * 
 * <p>For example, here's how to calculate the fingerprint of a file.  
 * This code is from the static function, 
 * <tt><font color=green>md5file(filename)</font></tt>:
 * <pre>
 *     ...
 *     MD5 md = new MD5();
 *     FileInputStream fis = new FileInputStream(filename);
 *     byte [] buff = new byte[1024];
 *     int nread;
 *     while ( (nread=fis.read(buff, 0, 1024)) >= 0 ) {
 *          md.addInput(buff, nread);
 *     }
 *     fis.close();
 *     byte [] fp = md.getMD();
 *     ...
 * </pre>
 * 
 * <p>You can only call the function <tt>getMD()</tt> once for a given
 * input sequence, as the end of the input is padded in a special way.
 * (E.g. you cannot get MD5 values on partial pieces of the data).
 * 
 * <p><h4>Implementation notes:</h4>
 * I started with the C source in the 
 * <a href="http://www.neda.com/rfc/rfc1320.txt">RFC 1320</a>
 * and manually converted it to Java.
 * For debuggin, I had to resort to compiling the C source to use
 * as a reference, adding print statements at various places.
 * 
 * <p>Major porting problems I encountered. <br>
 * (1) need to define an unsigned integer add function. <br>
 * (2) when packing 4 bytes --> integer, must convert bytes to unsigned
 *     in routine byte2int().<br>
 * (3) Stupidity: got lazy when transcribing the I() function.  <br>
 * 
 * <p>This code has not been optimized for speed in anyway.
 * (In some cases, its slowness maybe even be considered an asset.)
 * On Linux, this code runs roughly 10X faster under Kaffe 0.82,
 * than under JDK 1.0.2.
 * 
 * <p> This code is partly thread-safe, as the <tt>addInput()</tt> routines
 * are synchronized.  However, I'm not sure using this class in a multi
 * threaded application makes sense, as 
 * the implication is that there is an unpredictable order in which
 * data is processed, resulting in a fingerprint of marginal utility.
 *
 * <p>Testing: Passes the test suite 
 * and correctly calculates the digest/fingerprint
 * of the files <tt>MD5.java</tt> and <tt>MD5.class</tt>
 * 
 * <p> As a reference, here are the MD5 values for the reference strings
 * cited in the aforementioned RFC.
 * <pre>
 * MD5 ("") = d41d8cd98f00b204e9800998ecf8427e
 * MD5 ("a") = 0cc175b9c0f1b6a831c399e269772661
 * MD5 ("abc") = 900150983cd24fb0d6963f7d28e17f72
 * MD5 ("message digest") = f96b697d7cb7938d525a2f31aaf161d0
 * MD5 ("abcdefghijklmnopqrstuvwxyz") = c3fcd3d76192e4007dfb496cca67e13b
 * MD5 ("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789") = d174ab98d277d9f5a5611c2c9f419d9f
 * MD5 ("12345678901234567890123456789012345678901234567890123456789012345678901234567890") = 57edf4a22be3c955ac49da2e2107b67a
 * </pre>
 * 
 * <hr>
 * This code is distributed via 
 * <a href="http://www.best.com/~quong/java/qplmd.tgz">
 * a gzipped tar file (www.best.com/~quong/java/qplmd.tgz)
 * </a>
 *
 * <p>This code is distributed via 
 * <a href="LICENSE.txt">a BSD style license</a> (not GPL).
 * This code is free, but not public-domain.  I retain the copyright.
 * 
 * If you use this code, I would enjoy hearing from you.  
 * (Three weeks after releasing this code on Gamelan, 
 * I've heard from <strong>no one</strong>.)
 * 
 * <ADDRESS>
 * <A href="http://www.best.com/~quong">Russell W. Quong</A>
 * Apr 16, 1997
 * (<A href="mailto:quong@best.com"><SAMP>quong@best.com</SAMP></A>)
 * </ADDRESS>
 * 
 * @version Apr 16 1997, Version 1.0
 * @author Russell Quong
 * @org    VA Research
 */

public class MD5 implements MesgDigest {
	private static ConsoleLogger logger = new ConsoleLogger();
	private static final String CLASSNAME = "MD5";

	
	final static void print (String s) { logger.infoLog(CLASSNAME, "print()", s); }
	final static void println (String s) { logger.infoLog(CLASSNAME, "print()", s); }

	/**
	 * Static members
	 */
	final static byte S11 = 7;
	final static byte S12 = 12;
	final static byte S13 = 17;
	final static byte S14 = 22;
	final static byte S21 = 5;
	final static byte S22 = 9;
	final static byte S23 = 14;
	final static byte S24 = 20;
	final static byte S31 = 4;
	final static byte S32 = 11;
	final static byte S33 = 16;
	final static byte S34 = 23;
	final static byte S41 = 6;
	final static byte S42 = 10;
	final static byte S43 = 15;
	final static byte S44 = 21;

	// MesgDigestUtil = all static member fns, needn't instantiate
	static MesgDigestUtil util = null;

	static byte [] PADDING = new byte [64];
	static {
		for (int i=1; i<PADDING.length; i++) {
			PADDING[i] = 0;
		}
		PADDING[0] = (byte) 0x80;
	}

	static final int F (int x, int y, int z) { return (x & y) | ((~x) & z); }
	static final int G (int x, int y, int z) { return (x & z) | (y & (~z)); }
	static final int H (int x, int y, int z) { return (x^y)^z; }
	static final int I (int x, int y, int z) { return (y ^ (x | (~z))); }

	final static int FF (int a, int b, int c, int d, int x, int s, int ac) {
		a = util.uadd32(a, F(b, c, d), x, ac);
		a = util.rl(a, s);
		a = util.uadd32(a,b);
		return a;
	} /* FF */

	final static int GG (int a, int b, int c, int d, int x, int s, int ac) {
		a = util.uadd32(a, G(b, c, d), x, ac);
		a = util.rl(a, s);
		a = util.uadd32(a,b);
		return a;
	} /* FF */

	final static int HH (int a, int b, int c, int d, int x, int s, int ac) {
		a = util.uadd32(a, H(b, c, d), x, ac);
		a = util.rl(a, s);
		a = util.uadd32(a,b);
		return a;
	} /* HH */

	final static int II (int a, int b, int c, int d, int x, int s, int ac) {
		a = util.uadd32(a, I(b, c, d), x, ac);
		a = util.rl(a, s);
		a = util.uadd32(a,b);
		return a;
	} /* II */

	/*
	 * ****************************************************************
	 * Non-static members
	 * 
	 */ 

	int [] state = new int[4];  	/* state (ABCD) */
	int [] count = new int[2];         /* # of bits, modulo 2^64 (lsb first) */
	byte [] buffer = new byte[64];	/* input buffer */

	public void init () {
		count[0] = count[1] = 0;
		state[0] = 0x67452301;
		state[1] = 0xefcdab89;
		state[2] = 0x98badcfe;
		state[3] = 0x10325476;
		for (int i=0; i<buffer.length; i++) {
			buffer[i] = 0;
		}
	}

	public MD5 () {
		init();
	}

	/**
	 * Function: <tt>digestLen()</tt>
	 * return the number of bytes in the final digest.
	 */
	public int digestLen () {
		return 128/8;		// number of bytes
	}

	/**
	 * Function: <tt>addInput(String s)</tt>
	 * @param s		string containing (more) input data to process.
	 * 
	 * We only process the low 8 bits
	 * of each character.  (Sorry, UNICODE users --- I'm an ASCII pig.)
	 */
	public synchronized void addInput (String s) {
		int n = s.length();
		byte [] buff = new byte [n];
		for (int i=0; i<n; i++) {
			buff[i] = (byte) (s.charAt(i) & 0xff);
		}
		addInput(buff, n);
	} /* addInput */

	/**
	 * Function: <tt>addInput(byte [] input, int len)</tt>
	 * Process LEN bytes of input from INPUT.  This is the basic addInput() fn.
	 * 
	 * (Internal details) This is the MD5 block update operation. 
	 * Continues an MD5 message-digest operation, 
	 * processing another message block, and updating the context.
	 */
	public synchronized void addInput (byte [] input, int inputLen) {
		int i, index, partLen;

		/* Compute number of bytes mod 64 */
		index = ((this.count[0] >>> 3) & 0x3F);

		/* Update number of bits */
		this.count[0] += (inputLen << 3);
		if (this.count[0] < (inputLen << 3)) {
			this.count[1]++;
		}
		this.count[1] += (inputLen >>> 29);

		partLen = 64 - index;
		/* Transform as many times as possible. */
		if (inputLen >= partLen) {
			util.memcpy(this.buffer, index, input, 0, partLen);
			transform (this.buffer, 0);

			for (i = partLen; i + 63 < inputLen; i += 64) {
				transform (input, i);
			}
			index = 0;
		} else {
			i = 0;
		}

		/* 
		 * Buffer remaining input
		 */
		util.memcpy(this.buffer, index, input, i, inputLen-i);
	}

	/**
	 * Function: <tt>byte [] getMD()</tt>
	 * @return		the 128 bit fingerprint of the input 
	 * as a 16 byte array.
	 * 
	 * <p>Once called, the internal state is reset, so that 
	 * subsequent calls to <tt>addInput()</tt> are viewed as seperate input.
	 * 
	 * <p>(Internal details) MD5 finalization. 
	 * Ends an MD5 message-digest operation, saving the message digest.
	 */
	public byte [] getMD () { 
		byte [] bits = new byte [8];
		byte [] digest = new byte [16];
		int index, padLen;

		/* Save number of bits */
		util.int2byte(bits, this.count, 8);

		/* Pad out to 56 mod 64. */
		index = ((this.count[0] >>> 3) & 0x3f);
		padLen = (index < 56) ? (56 - index) : (120 - index);
		this.addInput(PADDING, padLen);

		/* Append length (before padding) */
		this.addInput(bits, 8);

		/* Store state in digest */
		util.int2byte(digest, this.state, 16);

		/* Reset internal state. */
		//        this.init();

		return digest;
	}

	/**
	 * MD5 basic transformation. Transforms state based on 64 byte blocks.
	 * Only called when we have enough input via addInput().
	 */
	void transform (byte [] block, int boff) {
		int a = this.state[0];
		int b = this.state[1];
		int c = this.state[2];
		int d = this.state[3];
		int [] x = new int[16];

		util.byte2int(x, block, boff, 64);

		/* Round 1 */
		a = FF(a, b, c, d, x[ 0], S11, 0xd76aa478); /* 1 */
		d = FF(d, a, b, c, x[ 1], S12, 0xe8c7b756); /* 2 */
		c = FF(c, d, a, b, x[ 2], S13, 0x242070db); /* 3 */
		b = FF(b, c, d, a, x[ 3], S14, 0xc1bdceee); /* 4 */
		a = FF(a, b, c, d, x[ 4], S11, 0xf57c0faf); /* 5 */
		d = FF(d, a, b, c, x[ 5], S12, 0x4787c62a); /* 6 */
		c = FF(c, d, a, b, x[ 6], S13, 0xa8304613); /* 7 */
		b = FF(b, c, d, a, x[ 7], S14, 0xfd469501); /* 8 */
		a = FF(a, b, c, d, x[ 8], S11, 0x698098d8); /* 9 */
		d = FF(d, a, b, c, x[ 9], S12, 0x8b44f7af); /* 10 */
		c = FF(c, d, a, b, x[10], S13, 0xffff5bb1); /* 11 */
		b = FF(b, c, d, a, x[11], S14, 0x895cd7be); /* 12 */
		a = FF(a, b, c, d, x[12], S11, 0x6b901122); /* 13 */
		d = FF(d, a, b, c, x[13], S12, 0xfd987193); /* 14 */
		c = FF(c, d, a, b, x[14], S13, 0xa679438e); /* 15 */
		b = FF(b, c, d, a, x[15], S14, 0x49b40821); /* 16 */

		/* Round 2 */
		a = GG(a, b, c, d, x[ 1], S21, 0xf61e2562); /* 17 */
		d = GG(d, a, b, c, x[ 6], S22, 0xc040b340); /* 18 */
		c = GG(c, d, a, b, x[11], S23, 0x265e5a51); /* 19 */
		b = GG(b, c, d, a, x[ 0], S24, 0xe9b6c7aa); /* 20 */
		a = GG(a, b, c, d, x[ 5], S21, 0xd62f105d); /* 21 */
		d = GG(d, a, b, c, x[10], S22,  0x2441453); /* 22 */
		c = GG(c, d, a, b, x[15], S23, 0xd8a1e681); /* 23 */
		b = GG(b, c, d, a, x[ 4], S24, 0xe7d3fbc8); /* 24 */
		a = GG(a, b, c, d, x[ 9], S21, 0x21e1cde6); /* 25 */
		d = GG(d, a, b, c, x[14], S22, 0xc33707d6); /* 26 */
		c = GG(c, d, a, b, x[ 3], S23, 0xf4d50d87); /* 27 */
		b = GG(b, c, d, a, x[ 8], S24, 0x455a14ed); /* 28 */
		a = GG(a, b, c, d, x[13], S21, 0xa9e3e905); /* 29 */
		d = GG(d, a, b, c, x[ 2], S22, 0xfcefa3f8); /* 30 */
		c = GG(c, d, a, b, x[ 7], S23, 0x676f02d9); /* 31 */
		b = GG(b, c, d, a, x[12], S24, 0x8d2a4c8a); /* 32 */

		/* Round 3 */
		a = HH(a, b, c, d, x[ 5], S31, 0xfffa3942); /* 33 */
		d = HH(d, a, b, c, x[ 8], S32, 0x8771f681); /* 34 */
		c = HH(c, d, a, b, x[11], S33, 0x6d9d6122); /* 35 */
		b = HH(b, c, d, a, x[14], S34, 0xfde5380c); /* 36 */
		a = HH(a, b, c, d, x[ 1], S31, 0xa4beea44); /* 37 */
		d = HH(d, a, b, c, x[ 4], S32, 0x4bdecfa9); /* 38 */
		c = HH(c, d, a, b, x[ 7], S33, 0xf6bb4b60); /* 39 */
		b = HH(b, c, d, a, x[10], S34, 0xbebfbc70); /* 40 */
		a = HH(a, b, c, d, x[13], S31, 0x289b7ec6); /* 41 */
		d = HH(d, a, b, c, x[ 0], S32, 0xeaa127fa); /* 42 */
		c = HH(c, d, a, b, x[ 3], S33, 0xd4ef3085); /* 43 */
		b = HH(b, c, d, a, x[ 6], S34,  0x4881d05); /* 44 */
		a = HH(a, b, c, d, x[ 9], S31, 0xd9d4d039); /* 45 */
		d = HH(d, a, b, c, x[12], S32, 0xe6db99e5); /* 46 */
		c = HH(c, d, a, b, x[15], S33, 0x1fa27cf8); /* 47 */
		b = HH(b, c, d, a, x[ 2], S34, 0xc4ac5665); /* 48 */

		/* Round 4 */
		a = II(a, b, c, d, x[ 0], S41, 0xf4292244); /* 49 */
		d = II(d, a, b, c, x[ 7], S42, 0x432aff97); /* 50 */
		c = II(c, d, a, b, x[14], S43, 0xab9423a7); /* 51 */
		b = II(b, c, d, a, x[ 5], S44, 0xfc93a039); /* 52 */
		a = II(a, b, c, d, x[12], S41, 0x655b59c3); /* 53 */
		d = II(d, a, b, c, x[ 3], S42, 0x8f0ccc92); /* 54 */
		c = II(c, d, a, b, x[10], S43, 0xffeff47d); /* 55 */
		b = II(b, c, d, a, x[ 1], S44, 0x85845dd1); /* 56 */
		a = II(a, b, c, d, x[ 8], S41, 0x6fa87e4f); /* 57 */
		d = II(d, a, b, c, x[15], S42, 0xfe2ce6e0); /* 58 */
		c = II(c, d, a, b, x[ 6], S43, 0xa3014314); /* 59 */
		b = II(b, c, d, a, x[13], S44, 0x4e0811a1); /* 60 */
		a = II(a, b, c, d, x[ 4], S41, 0xf7537e82); /* 61 */
		d = II(d, a, b, c, x[11], S42, 0xbd3af235); /* 62 */
		c = II(c, d, a, b, x[ 2], S43, 0x2ad7d2bb); /* 63 */
		b = II(b, c, d, a, x[ 9], S44, 0xeb86d391); /* 64 */

		this.state[0] = util.uadd32(this.state[0], a);
		this.state[1] = util.uadd32(this.state[1], b);
		this.state[2] = util.uadd32(this.state[2], c);
		this.state[3] = util.uadd32(this.state[3], d);
	}

	/**
	 * Static function: <tt>main()</tt>
	 * @param	args = array of strings - the command line parameters
	 * <ul>
	 * <li>With no command-line parameters, 
	 *   calculate and prints the MD5 fingerprint of standard test strings.
	 * <li>Otherwise, prints the MD5 fingerprint of each of the files
	 *   on the command line.
	 * </ul>
	 */
	public static void main (String [] args) {
		int n = args.length;
		if (n == 0) {
			testSuite();
		} else {
			for (int i=0; i<n; i++) {
				printMD5File(args[i]);
			}
		}
	}

	/**
	 * Static function: <tt>testSuite()</tt>
	 * Print the MD5 fingerprint of some reference strings from
	 * <a href="http://www.neda.com/rfc/rfc1320.txt">RFC 1320</a>
	 */
	public static void testSuite () {
		println("MD5 test suite");

		printMD5Str("");
		printMD5Str("a");
		printMD5Str("abc");
		printMD5Str("message digest");
		printMD5Str("abcdefghijklmnopqrstuvwxyz");
		printMD5Str(
				"ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789");
		printMD5Str("12345678901234567890123456789012345678901234567890123456789012345678901234567890");
	}

	/**
	 * Static function: <tt>md5str(String s)</tt>
	 * Generate the MD5 fingerprint for string s, returns result as byte array.
	 */
	public static byte [] md5str (String s) {
		MesgDigest md = new MD5();
		md.addInput(s);
		return md.getMD();
	} /* md5str */

	/**
	 * Static function: <tt>printMD5Str(String s)</tt>
	 * Generate the MD5 fingerprint for string s, and print it out in hex.
	 */
	public static void printMD5Str (String s) {
		MD5 md = new MD5();
		md.addInput(s);
		byte [] fp = md.getMD();
		String os = util.bytes2hexStr(fp);

		println("MD5(\"" + s + "\") = " + os);
	} /* printMD5Str */

	/**
	 * Static function: <tt>md5file (String filename)</tt>
	 * Returns the MD5 FP of the contents of the file FILENAME as a byte array.
	 */
	public static byte [] md5file (String filename) {
		try (FileInputStream fis = new FileInputStream(filename);) {

			byte [] buff = new byte[1024];

			MesgDigest md = new MD5();
			int nread;
			while ( (nread=fis.read(buff, 0, 1024)) >= 0 ) {
				md.addInput(buff, nread);
			}
			fis.close();

			byte [] fp = md.getMD();
			return fp;
		} catch (IOException e) {
			logger.errorLog(CLASSNAME, "md5file()", "Default Key Found.", e);
		}
		return null;
	}

	public static String crypt(String unencryptedPassword){
		MesgDigest md = new MD5();
		md.addInput(unencryptedPassword);
		byte [] fp = md.getMD();
		String os = util.bytes2hexStr(fp);
		return os;
	}

	/**
	 * 
	 * Static function: <tt>printMD5File (String filename)</tt>
	 * Generate the MD5 FP of the contents of the file FILENAME, and print it.
	 */
	public static void printMD5File (String filename) {
		byte [] fp = md5file(filename);
		String os = util.bytes2hexStr(fp);
		println("MD5(" + filename + ") = " + os);
	}

}
