package  com.styf.netty.net;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CommonMessage
        implements Serializable
{
  private static final Logger LOGGER = LoggerFactory.getLogger(CommonMessage.class);
  private static final long serialVersionUID = 1L;
  public static final short HDR_SIZE = 8;
  public static final short HEADER = 29099;
  private short checksum;
  private short code;
  private byte[] bodyData = null;

  private CommonMessage()
  {
  }

  public CommonMessage(short code)
  {
    this.code = code;
  }

  public static CommonMessage build(byte[] msgData, int length)
  {
    CommonMessage builder = new CommonMessage();
    if ((msgData == null) || (length < 8) || (length > 32767))
    {
      return null;
    }

    ByteBuffer buff = ByteBuffer.wrap(msgData);

    buff.position(4);
    builder.checksum = buff.getShort();
    builder.code = buff.getShort();
    int bodyLen = length - 8;
    if (bodyLen > 0)
    {
      builder.bodyData = new byte[bodyLen];
      buff.get(builder.bodyData, 0, bodyLen);

      short getCS = builder.calcChecksum(msgData, length);
      if (builder.checksum != getCS)
      {
        LOGGER.warn("数据包校验失败，数据包将被丢弃。code: 0x{}。校验和应为{}，实际接收校验和为{}", new Object[] {
                Short.valueOf(builder.getCode()), Short.valueOf(getCS), Short.valueOf(builder.checksum) });
        return null;
      }
    }
    else
    {
      builder.bodyData = null;
    }

    return builder;
  }

  public ByteBuffer toByteBuffer()
  {
    short len = getLen();
    ByteBuffer buff = ByteBuffer.allocate(len);
    buff.putShort((short)29099);

    buff.putShort(len);

    buff.position(6);
    buff.putShort(this.code);
    if (this.bodyData != null)
    {
      buff.put(this.bodyData);
    }
    int pos = buff.position();

    buff.position(4);
    buff.putShort(calcChecksum(buff.array(), len));

    buff.position(pos);

    buff.flip();
    return buff;
  }

  public short getLen()
  {
    short bodyLen = this.bodyData == null ? 0 : (short)this.bodyData.length;
    return (short)(8 + bodyLen);
  }

  public short getChecksum()
  {
    return this.checksum;
  }

  public short getCode()
  {
    return this.code;
  }

  public void setCode(short code)
  {
    this.code = code;
  }

  public byte[] getBody()
  {
    return this.bodyData;
  }

  public void setBody(byte[] bytes)
  {
    this.bodyData = bytes;
  }

  private short calcChecksum(byte[] data, int length)
  {
    int val = 119;
    int i = 6;

    while (i < length)
    {
      val += (data[(i++)] & 0xFF);
    }
    return (short)(val & 0x7F7F);
  }

  public String headerToStr()
  {
    StringBuilder sb = new StringBuilder();
    sb.append("len: ").append(getLen());
    sb.append(", checksum: ").append(this.checksum);
    sb.append(", code: 0x").append(Integer.toHexString(this.code));

    return sb.toString();
  }

  public String detailToStr()
  {
    String str = "";
    if (this.bodyData != null)
    {
      try
      {
        str = new String(this.bodyData, "UTF-8");
      }
      catch (UnsupportedEncodingException e)
      {
        str = "(UnsupportedEncodingException)";
      }
    }
    return String.format("%s. content:%s.", new Object[] { headerToStr(), str });
  }
}