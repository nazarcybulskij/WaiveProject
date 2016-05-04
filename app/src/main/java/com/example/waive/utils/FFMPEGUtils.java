package com.example.waive.utils;

import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.example.waive.R;
import com.example.waive.WaivelengthApplication;
import com.github.hiteshsondhi88.libffmpeg.FFmpeg;
import com.github.hiteshsondhi88.libffmpeg.FFmpegExecuteResponseHandler;
import com.github.hiteshsondhi88.libffmpeg.FFmpegLoadBinaryResponseHandler;
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegCommandAlreadyRunningException;
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegNotSupportedException;

import java.io.File;
import java.util.Locale;


public class FFMPEGUtils {
    private static final String TAG = FFMPEGUtils.class.getSimpleName();
    private static final String OUTPUT = Environment.getExternalStorageDirectory() + "/trim_output.mp4";

    private static void showToast(int text) {
        Toast.makeText(WaivelengthApplication.getInstance(), text, Toast.LENGTH_LONG).show();
    }

    public static void initFFMPEG() {
        try {
            FFmpeg.getInstance(WaivelengthApplication.getInstance()).loadBinary(new FFmpegLoadBinaryResponseHandler() {
                @Override
                public void onFailure() {
                    showToast(R.string.load_failed);
                }

                @Override
                public void onSuccess() {
                    Log.d(TAG, "FFMPEG loaded");
                }

                @Override
                public void onStart() {
                }

                @Override
                public void onFinish() {
                }
            });
        } catch (FFmpegNotSupportedException e) {
            showToast(R.string.device_not_supported_message);
        }
    }

    private static String[] createTrimCommand(String videoPath, int start, int end) {
        return String.format(Locale.US, "-i %s -ss %d -t %d -acodec copy -vcodec copy %s", videoPath, start, end - start, OUTPUT).split(" ");
    }
    
    public static void trimVideo(String videoPath, int start, int end, final Callback callback) {
        try {
            new File(OUTPUT).delete();
            FFmpeg.getInstance(WaivelengthApplication.getInstance()).killRunningProcesses();
            FFmpeg.getInstance(WaivelengthApplication.getInstance()).execute(createTrimCommand(videoPath, start, end),
                    new FFmpegExecuteResponseHandler() {
                        @Override
                        public void onSuccess(String message) {
                            Log.d(TAG, "finished trim");
                            callback.finished(OUTPUT);
                        }

                        @Override
                        public void onProgress(String message) {
                            Log.d(TAG, message);
                        }

                        @Override
                        public void onFailure(String message) {
                            Log.d(TAG, "error: " + message);
                            callback.error();
                            showToast(R.string.error);
                        }

                        @Override
                        public void onStart() {
                        }

                        @Override
                        public void onFinish() {
                        }
                    });
        } catch (FFmpegCommandAlreadyRunningException e) {
            showToast(R.string.another_command_is_running);
        }
    }

    public interface Callback {
        void finished(String file);

        void error();
    }
}
