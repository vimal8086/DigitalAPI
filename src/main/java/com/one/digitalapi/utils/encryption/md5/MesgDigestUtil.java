/** 
 * Java file: MesgDigestUtil.java
 * @version May 13 1997
 * @author Russell Quong
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

import com.one.digitalapi.logger.ConsoleLogger;

public final class MesgDigestUtil {
	
	private static ConsoleLogger logger = new ConsoleLogger();
	private static final String CLASSNAME = "MesgDigestUtil";

	
    final static void print (String s) { logger.infoLog(CLASSNAME, "print()", s); }
    final static void println (String s) { logger.infoLog(CLASSNAME, "print()", s); }

    //#define ROTATE_LEFT(x, n) (((x) << (n)) | ((x) >> (32-(n))))
    /**
     *  ROTATE_LEFT rotates x left n bits.
     */
    final static int rl (int x, int n) {
	return (x<<n) | (x>>>(32-n));
    }

    final static int uadd32 (int x, int y) {	return uadd32(x,y,0,0);    }
    final static int uadd32 (int x, int y, int z) { return uadd32(x,y,z,0);  }
    final static int uadd32 (int v, int x, int y, int z) {
	
	
	
	
	long res = v + x + y + z + 0x400000000L;
	return (int) (res & 0xffffffffL);
    } /* uadd32 */

    public final static void memcpy (byte [] dest, int doff, byte [] src, int soff, int len) {
	for (int i=0; i<len; i++) {
	    dest[doff+i] = src[soff+i];
	}
    }

    public final static void memset (int [] arr, int offset, int value, int len) {
	for (int i = offset; i < offset+len; i++) {
	    arr[i] = value;
	}
    }

    final static int ubyte (byte b) {
	return (b < 0) ? (256+b) : b;
    }

    /**
     * Encodes input (int) into output (bytes). 
     * Assumes len is a multiple of 4.
     */
    final static void int2byte (byte [] output, int [] input, int len) {
	int i = 0;
	if ((len % 4) != 0) {
	    println("Error: int2byte(..., len), len not multiple of 4");
	    println("Error: result will probably be wrong");
	}
	for (int j = 0; j < len; j += 4) {
	    output[j+0] = (byte) (input[i] & 0xff);
	    output[j+1] = (byte) ((input[i] >>> 8) & 0xff);
	    output[j+2] = (byte) ((input[i] >>> 16) & 0xff);
	    output[j+3] = (byte) ((input[i] >>> 24) & 0xff);
	    i++;
	}
    }

    /**
     * Byte2ints input (bytes) into output (int). 
     * Assumes len is a multiple of 4.
     */
    final static void byte2int (int [] output, byte [] input, int ioff, int len) {
	int i = 0;
	for (int j = ioff; j < (ioff+len); j += 4) {
	    // This code doesn't work.  
	    // Well yes it does, *NOW* as I mask each byte with 0xff.
	    // Took me the longest time to figure this out.
	    output[i] =
		( input[j+0] & 0xff) |
		((input[j+1] & 0xff) << 8) |
		((input[j+2] & 0xff) << 16) |
		((input[j+3] & 0xff) << 24);
	    i++;
	}
    }


    private static final char [] hex = {
	'0', '1', '2', '3', '4', '5', '6', '7',
	'8', '9', 'a', 'b', 'c', 'd', 'e', 'f'
    };

    /**
     * Function: <tt>bytes2hexStr (byte [] arr, int len)(</tt>
     * Generate a hex string showing the value of the bytes in ARR.
     * (Used by me for testing this class.)
     */
    public final static String bytes2hexStr (byte [] arr, int len) {
	StringBuffer sb = new StringBuffer(len*2);
        for (int i=0; i<len; i++) {
	    int hi = (arr[i]>>>4) & 0xf;
	    sb.append( hex[hi] );
	    int low = (arr[i]) & 0xf;
	    sb.append( hex[low] );
	}
	return sb.toString();
    }

    public static String bytes2hexStr (byte [] arr) {
	return bytes2hexStr(arr, arr.length);
    }

    // debugging function
    private static void pr (String s, int i) {
	long l = i;
	if (l < 0) {
	    l += 0x100000000L;
	}
	logger.errorLog(CLASSNAME, "bytes2hexStr()", s + Long.toString(l, 16) );
    } /* pr */

}	
