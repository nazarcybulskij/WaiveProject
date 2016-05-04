package com.example.waive.utils;

import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

//import org.mp4parser.IsoFile;
//import org.mp4parser.muxer.FileDataSourceImpl;
//import org.mp4parser.muxer.Movie;
//import org.mp4parser.muxer.Track;
//import org.mp4parser.muxer.container.mp4.MovieCreator;
//import org.mp4parser.muxer.tracks.h264.H264TrackImpl;

public class VideoUtils {
	
//	public static void trim(String srcFilePath, String dstFilePath, int startMs, int endMs) throws IOException{
//		
//        FileInputStream fileInputStream = new FileInputStream(new File(srcFilePath));
//        H264TrackImpl h264Track = new H264TrackImpl(new FileDataSourceImpl(srcFilePath));
//        IsoFile isoFile = new IsoFile(fileInputStream.getChannel());
//        
//        Movie movie = MovieCreator.build(srcFilePath);
//        List<Track> tracks = movie.getTracks();
//	}
	
	
//    public static void trim(File src, File dst, int startMs, int endMs) throws IOException {
//
//    	H264TrackImpl h264Track = new H264TrackImpl(new FileDataSourceImpl("video.h264"));
//    	
//    	
//    	//RandomAccessFile randomAccessFile = new RandomAccessFile(src, "r");
//        Movie movie = MovieCreator.build(/*randomAccessFile.getChannel()*/src.getAbsolutePath());
//        // remove all tracks we will create new tracks from the old
//        List<Track> tracks = movie.getTracks();
//        movie.setTracks(new LinkedList<Track>());
//        double startTime = startMs/1000;
//        double endTime = endMs/1000;
//        boolean timeCorrected = false;
//        // Here we try to find a track that has sync samples. Since we can only start decoding
//        // at such a sample we SHOULD make sure that the start of the new fragment is exactly
//        // such a frame
//        for (Track track : tracks) {
//            if (track.getSyncSamples() != null && track.getSyncSamples().length > 0) {
//                if (timeCorrected) {
//                    // This exception here could be a false positive in case we have multiple tracks
//                    // with sync samples at exactly the same positions. E.g. a single movie containing
//                    // multiple qualities of the same video (Microsoft Smooth Streaming file)
//                    throw new RuntimeException("The startTime has already been corrected by another track with SyncSample. Not Supported.");
//                }
//                startTime = correctTimeToSyncSample(track, startTime, false);
//                endTime = correctTimeToSyncSample(track, endTime, true);
//                timeCorrected = true;
//            }
//        }
//        for (Track track : tracks) {
//            long currentSample = 0;
//            double currentTime = 0;
//            long startSample = -1;
//            long endSample = -1;
//            for (int i = 0; i < track.getCompositionTimeEntries().size(); i++) {
//            	CompositionTimeToSample.Entry entry = track.getCompositionTimeEntries().get(i);
//                for (int j = 0; j < entry.getCount(); j++) {
//                    // entry.getDelta() is the amount of time the current sample covers.
//                    if (currentTime <= startTime) {
//                        // current sample is still before the new starttime
//                        startSample = currentSample;
//                    }
//                    if (currentTime <= endTime) {
//                        // current sample is after the new start time and still before the new endtime
//                        endSample = currentSample;
//                    } else {
//                        // current sample is after the end of the cropped video
//                        break;
//                    }
//                    currentTime += (double) entry.getOffset() / (double) track.getTrackMetaData().getTimescale();
//                    currentSample++;
//                }
//            }
//            movie.addTrack(new CroppedTrack(track, startSample, endSample));
//        }
//        
//        IsoFile out = (IsoFile) new DefaultMp4Builder().build(movie);
//        if (!dst.exists()) {
//            dst.createNewFile();
//        }
//        FileOutputStream fos = new FileOutputStream(dst);
//        FileChannel fc = fos.getChannel();
//        out.getBox(fc);  // This one build up the memory.
//        fc.close();
//        fos.close();
//        //randomAccessFile.close();
//    }
//    
//    protected void merge(ArrayList<String> moviePaths) throws IOException{
//
//    	ArrayList<Movie> clips = new ArrayList<Movie>();
//    	
//        for(int i=0; i<moviePaths.size();i++){
//
//                Movie tm = MovieCreator.build(moviePaths.get(i));
//                clips.add(tm);
//        }
//
//         List<Track> videoTracks = new LinkedList<Track>();
//         List<Track> audioTracks = new LinkedList<Track>();
//
//            for (Movie m : clips) {
//                for (Track t : m.getTracks()) {
//                    if (t.getHandler().equals("soun")) {
//                        audioTracks.add(t);
//                    }
//                    if (t.getHandler().equals("vide")) {
//                        videoTracks.add(t);
//                    }
//                }
//            }
//
//            Movie result = new Movie();
//            Log.v("cam", "adding:"+audioTracks.size()+" audio tracks and "+videoTracks.size()+" video tracks");
//            if (audioTracks.size() > 0) {
//                result.addTrack(new AppendTrack(audioTracks.toArray(new Track[audioTracks.size()])));
//            }
//            if (videoTracks.size() > 0) {
//                result.addTrack(new AppendTrack(videoTracks.toArray(new Track[videoTracks.size()])));
//            }
//
//            Container out = new DefaultMp4Builder().build(result);
//
//            String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES) + "waivelength";
//            FileChannel fc = new RandomAccessFile(path + "/merge.mp4", "rw").getChannel();
//            out.writeContainer(fc);
//            fc.close();
//
//    }
//    
//    protected static long getDuration(Track track) {
//        long duration = 0;
//        for (CompositionTimeToSample.Entry entry : track.getCompositionTimeEntries()) {
//            duration += entry.getCount() * entry.getOffset();
//        }
//        return duration;
//    }
//    private static double correctTimeToSyncSample(Track track, double cutHere, boolean next) {
//        double[] timeOfSyncSamples = new double[track.getSyncSamples().length];
//        long currentSample = 0;
//        double currentTime = 0;
//        for (int i = 0; i < track.getCompositionTimeEntries().size(); i++) {
//        	CompositionTimeToSample.Entry entry = track.getCompositionTimeEntries().get(i);
//            for (int j = 0; j < entry.getCount(); j++) {
//                if (Arrays.binarySearch(track.getSyncSamples(), currentSample + 1) >= 0) {
//                    // samples always start with 1 but we start with zero therefore +1
//                    timeOfSyncSamples[Arrays.binarySearch(track.getSyncSamples(), currentSample + 1)] = currentTime;
//                }
//                currentTime += (double) entry.getOffset() / (double) track.getTrackMetaData().getTimescale();
//                currentSample++;
//            }
//        }
//        double previous = 0;
//        for (double timeOfSyncSample : timeOfSyncSamples) {
//            if (timeOfSyncSample > cutHere) {
//                if (next) {
//                    return timeOfSyncSample;
//                } else {
//                    return previous;
//                }
//            }
//            previous = timeOfSyncSample;
//        }
//        return timeOfSyncSamples[timeOfSyncSamples.length - 1];
//    }
}
