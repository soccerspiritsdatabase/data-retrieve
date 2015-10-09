package paek.kevin.ssdata.utils;

public class RC4 {

  private static final byte[] ENCRYPTION_KEY = "1tkdne#:Qho*tL@n6^Tn".getBytes();
  private static final int SKIP = 0x3f5;

  public static byte[] decrypt(final byte[] input) {
    RC4 rc4 = new RC4(ENCRYPTION_KEY);
    return rc4.crypt(input, SKIP);
  }

  private final int[] S = new int[256];

  public RC4(final byte[] key) {
    if (key.length < 1 || key.length > 256) {
      throw new IllegalArgumentException(
              "key must be between 1 and 256 bytes");
    } else {
      int[] buffer = new int[256];
      for (int i = 0; i < 256; i++) {
        S[i] = i;
        buffer[i] = key[i % key.length] & 0xff;
      }

      int j = 0;
      for (int i = 0; i < 256; i++) {
        j = (j + S[j] + buffer[j]) & 0xff;
        int tmp = S[i];
        S[i] = S[j];
        S[j] = tmp;
      }
    }
  }

  public byte[] crypt(final byte[] input, final int skip) {
    final byte[] output = new byte[input.length];

    int i = 0, j = 0, k, t;
    for (int counter = -skip; counter < input.length; counter++) {
      i = (i + 1) & 0xff;
      int tmp = S[i];
      j = (j + tmp) & 0xff;
      S[i] = S[j];
      S[j] = tmp;

      if (counter >= 0) {
        t = (S[i] + S[j]) & 0xFF;
        k = S[t];
        output[counter] = (byte) (input[counter] ^ k);
      }
    }

    return output;
  }
}
