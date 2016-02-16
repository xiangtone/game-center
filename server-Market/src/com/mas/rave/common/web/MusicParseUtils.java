package com.mas.rave.common.web;

import java.io.File;
import java.io.IOException;

import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.AudioHeader;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.tag.TagException;

public class MusicParseUtils {
	public static void main(String[] args) {
		String filePath = "G:\\resource\\music\\Psy - 江南style.m4a";
		File file = new File(filePath);
		MusicParseUtils musicParse = new MusicParseUtils();
		AudioHeader ah = musicParse.getAudioHeader(file);	
			System.out.println(ah.getFormat());
			System.out.println(ah.getChannels());
			System.out.println(ah.getTrackLength());	
			
			System.out.println(file.length());
	}
	
	public AudioHeader getAudioHeader(File file){
		try {
			AudioFile af = AudioFileIO.read(file);
			AudioHeader ah =af.getAudioHeader();
			return ah;

		} catch (CannotReadException | IOException | TagException
				| ReadOnlyFileException | InvalidAudioFrameException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
 
}
