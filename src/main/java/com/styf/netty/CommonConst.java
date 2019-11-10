package com.styf.netty;

import com.styf.netty.net.IClientConnection;
import io.netty.util.AttributeKey;

public final class CommonConst {
    public static final AttributeKey<IClientConnection> CLIENT_CON = AttributeKey.valueOf("CLIENT_CON");

    public static final AttributeKey<IServerConnector> SERVER_CON = AttributeKey.valueOf("SERVER_CON");

    public static final AttributeKey<int[]> NETTY_ENCRYPTION_KEY = AttributeKey.valueOf("EncryptionKey");

    public static final AttributeKey<int[]> NETTY_DECRYPTION_KEY = AttributeKey.valueOf("DecryptionKey");

    public static final AttributeKey<String> CLIENT_IP = AttributeKey.valueOf("CLIENT_IP");

    public static final AttributeKey<byte[]> READ_BYTES = AttributeKey.valueOf("READ_BYTES");
}
