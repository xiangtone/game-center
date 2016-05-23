
package com.hykj.gamecenter.utils;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import com.hykj.gamecenter.logic.entry.AuthorityInfo;
import com.hykj.gamecenter.utils.Interface.AuthorityParser;

public class AuthorityPullParser implements AuthorityParser {

    @Override
    public List<AuthorityInfo> parse(InputStream is) throws Exception {
        // TODO Auto-generated method stub
        List<AuthorityInfo> list = null;
        AuthorityInfo sourse = null;

        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
        XmlPullParser parser = factory.newPullParser();
        parser.setInput(is, "UTF-8");

        int eventType = parser.getEventType();
        while (eventType != XmlPullParser.END_DOCUMENT) {
            switch (eventType) {
                case XmlPullParser.START_DOCUMENT:
                    list = new ArrayList<AuthorityInfo>();
                    break;

                case XmlPullParser.START_TAG:
                    if (parser.getName().equals("Row")) {
                        sourse = new AuthorityInfo();
                    } else if (parser.getName().equals("Name")) {
                        eventType = parser.next();
                        sourse.setName(parser.getText().trim());
                        //                        String text = parser.getText().trim();
                        //                        Log.i("tom", "text === " + text);
                    } else if (parser.getName().equals("Flag")) {
                        eventType = parser.next();
                        sourse.setFlag(parser.getText().trim());
                        //                        String text = parser.getText().trim();
                        //                        Log.i("tom", "text === " + text);
                    } else if (parser.getName().equals("Des")) {
                        eventType = parser.next();
                        sourse.setDesp(parser.getText().trim());
                        //                        String text = parser.getText().trim();
                        //                        Log.i("tom", "text === " + text);
                    }
                    break;
                case XmlPullParser.END_TAG:
                    if (parser.getName().equals("Row")) {
                        list.add(sourse);
                        sourse = null;
                    }
                    break;
            }
            eventType = parser.next();
        }

        return list;
    }

}
