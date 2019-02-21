package com.fancy.util.媒体工具;

import com.alibaba.fastjson.JSONObject;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.audio.flac.FlacFileReader;
import org.jaudiotagger.audio.mp3.MP3File;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.TagException;
import org.jaudiotagger.tag.datatype.Artwork;
import org.jaudiotagger.tag.id3.AbstractID3v2Frame;
import org.jaudiotagger.tag.id3.AbstractID3v2Tag;
import org.jaudiotagger.tag.id3.ID3v23Frame;
import org.jaudiotagger.tag.id3.framebody.FrameBodyAPIC;
import sun.misc.BASE64Encoder;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * 获得MP3文件的信息
 */
public class MP3Info {

    public static void main(String[] args) {
        String mp3path = "F:\\music\\G.E.M.邓紫棋 - 岩石里的花.mp3";
        Tag tag = Mp3InfoRead(mp3path);
        getMp3Picture(mp3path);
        System.out.println(JSONObject.toJSONString(tag));
    }

    public static Tag Mp3InfoRead(String path) {
        MP3File file;
        try {
            file = new MP3File(path);

            String songName = ((ID3v23Frame) file.getID3v2Tag().frameMap.get("TIT2")).getContent();
            String artist = ((ID3v23Frame) file.getID3v2Tag().frameMap.get("TPE1")).getContent();
            String album = ((ID3v23Frame) file.getID3v2Tag().frameMap.get("TALB")).getContent();
            String length = file.getMP3AudioHeader().getTrackLengthAsString();

            Tag tag = new Tag();
            tag.setSongName(songName);
            tag.setAlbum(album);
            tag.setArtist(artist);
            tag.setLength(length);
            return tag;
        } catch (IOException | TagException | ReadOnlyFileException | InvalidAudioFrameException e) {
            e.printStackTrace();
            throw new RuntimeException("获取Mp3 tag信息出错！");

        }
    }

    public static void getMp3Picture(String mp3path) {
        Image img;
        try {
            String url = mp3path;
            File sourceFile = new File(url);
            MP3File mp3file = new MP3File(sourceFile);

            AbstractID3v2Tag tag = mp3file.getID3v2Tag();
            AbstractID3v2Frame frame = (AbstractID3v2Frame) tag.getFrame("APIC");
            FrameBodyAPIC body = (FrameBodyAPIC) frame.getBody();
            byte[] imageData = body.getImageData();
            img = Toolkit.getDefaultToolkit().createImage(imageData, 0, imageData.length);
            ImageIcon icon = new ImageIcon(img);
            String storePath = mp3path;
            storePath = storePath.substring(0, storePath.length() - 3);
            storePath += "jpg";
            FileOutputStream fos = new FileOutputStream(storePath);
            fos.write(imageData);
            fos.close();
            BASE64Encoder encoder = new BASE64Encoder();
            String imageString = encoder.encode(imageData);
            System.out.println("data:image/jpg;base64," + imageString);//返回Base64编码过的字节数组字符串
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            throw new RuntimeException("读取Mp3图片出错");
        }
    }

    public static Tag FlacInfoRead(String path) {
        try {
            FlacFileReader fileReader = new FlacFileReader();
            AudioFile read = fileReader.read(new File(path));
            org.jaudiotagger.tag.Tag tag = read.getTag();
            String songName = tag.getFirst(FieldKey.TITLE);
            String artist = tag.getFirst(FieldKey.ARTIST);
            String album = tag.getFirst(FieldKey.ALBUM);
            int trackLength = read.getAudioHeader().getTrackLength();
            int min = trackLength / 60;
            int second = trackLength % 60;
            String length = min + ":" + second;
            System.out.println("长度:" + length);
            Tag tag2 = new Tag();
            tag2.setSongName(songName);
            tag2.setArtist(artist);
            tag2.setAlbum(album);
            return tag2;
//          System.out.println(songName);
//          System.out.println(artist);
//          System.out.println(album);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            throw new RuntimeException("读取Flac信息时出错！");
        }
    }

    public static Image getFlacPicture(String flacpath) {
        try {
            FlacFileReader fileReader = new FlacFileReader();
            AudioFile read = fileReader.read(new File(flacpath));
            org.jaudiotagger.tag.Tag tag = read.getTag();
            Artwork firstArtwork = tag.getFirstArtwork();
            byte[] imageData = firstArtwork.getBinaryData();
            Image image = Toolkit.getDefaultToolkit().createImage(imageData, 0, imageData.length);
            ImageIcon icon = new ImageIcon(image);
            String storePath = flacpath;
            storePath = storePath.substring(0, storePath.length() - 4);
            storePath += "jpg";
            System.out.println(storePath);
            FileOutputStream fos = new FileOutputStream(storePath);
            fos.write(imageData);
            fos.close();
            return image;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("读取Flac图片时出错！");
        }
    }

    public static class Tag {
        private String SongName;
        private String Artist;
        private String Album;
        private String length;

        public String getSongName() {
            return SongName;
        }

        public String getArtist() {
            return Artist;
        }

        public void setArtist(String artist) {
            Artist = artist;
        }

        public void setSongName(String songName) {
            SongName = songName;
        }

        public String getAlbum() {
            return Album;
        }

        public void setAlbum(String album) {
            Album = album;
        }

        public String getLength() {
            return length;
        }

        public void setLength(String length) {
            this.length = length;
        }


    }

}
