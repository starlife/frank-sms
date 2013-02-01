package com.unicom.mm7.frame;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.cmcc.mm7.vasp.http.HttpRequest;
import com.cmcc.mm7.vasp.protocol.DecodeMM7;
import com.cmcc.mm7.vasp.protocol.message.MM7DeliverReq;
import com.cmcc.mm7.vasp.protocol.message.MM7RSReq;
import com.cmcc.mm7.vasp.protocol.message.MM7VASPErrorRes;
import com.cmcc.mm7.vasp.protocol.message.MM7VASPRes;
import com.cmcc.mm7.vasp.protocol.util.Hex;
import com.unicom.mm7.bean.MmsFile;
import com.unicom.mm7.bean.UMms;

public class Test
{
	private static final Log log = LogFactory.getLog(Test.class);

	public static void main(String[] args) throws IOException
	{
		log.debug("本地接收前");
		byte[] bytes = Hex
				.rstr("");
		ByteArrayInputStream input = new ByteArrayInputStream(bytes);
		MM7VASPRes res = null;
		HttpRequest http = new HttpRequest();
		if (http.recvData(input))
		{
			// log.info("接收到数据包：" +
			// com.cmcc.mm7.vasp.protocol.util.Hex.rhex(http.getData()));
			// log.info(http.toString(Charset.forName("UTF-8")));
			// log.info("http.getData().length:"+http.getData().length);
			// log.info("bytes.length:"+bytes.length);
			// log.info("Hex.rhex(http.getHeader().getBytes()):"+Hex.rhex(http.getHeader().getBytes()));
			// log.info("http.getHeader().getBytes().length:"+http.getHeader().getBytes().length);
			// log.info("http.getBody().length:"+http.getBody().length);
			// log.info("Hex.rhex(http.getBody()):"+Hex.rhex(http.getBody()));
			MM7RSReq req = DecodeMM7.decodeReqMessage(http.getBody(), "UTF-8",
					DecodeMM7.getBoundary(http.getHeader()));
			MM7DeliverReq deliverReq = (MM7DeliverReq) req;
			MmsFile mmsFile = Receiver.makeMmsFile(deliverReq);
			UMms mms = Receiver.makeMms(mmsFile, deliverReq);
			// log.info(mms);
			Result.getInstance().notifyResult(mms);

		}
		else
		{
			// 发送一个错误回应包
			res = new MM7VASPErrorRes();
			res.setStatusCode(-102);
			res.setStatusText("接收回应消息失败！");
		}

	}
}
