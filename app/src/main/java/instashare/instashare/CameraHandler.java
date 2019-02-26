package instashare.instashare;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Camera;
import android.graphics.ImageFormat;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.TotalCaptureResult;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.Image;
import android.media.ImageReader;
import android.os.Environment;
import android.os.Handler;
import android.os.Parcel;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CameraHandler {


    CameraCaptureSession ccsession;
    SurfaceView sv;
    final CameraManager cm;
    Activity a;
    CameraDevice cd;
    Boolean takePicture = false;
    ImageReader ir;
    Surface irsurface;





    public CameraHandler(SurfaceView sv, Activity a) throws CameraAccessException {
        this.a = a;
        this.sv = sv;

        final Activity getActivity = a;
        cm = (CameraManager) a.getSystemService(Context.CAMERA_SERVICE);
        ir = ImageReader.newInstance(cm.getCameraCharacteristics(cm.getCameraIdList()[1])
                        .get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP)
                        .getOutputSizes(SurfaceHolder.class)[0].getWidth(),
                cm.getCameraCharacteristics(cm.getCameraIdList()[0])
                        .get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP)
                        .getOutputSizes(SurfaceHolder.class)[0].getHeight(), ImageFormat.JPEG, 1);
        final SurfaceView finalview = sv;
         SurfaceHolder surfhold = finalview.getHolder();
        surfhold.setKeepScreenOn(true);
        try {
            surfhold.setFixedSize(cm.getCameraCharacteristics(cm.getCameraIdList()[1])
                            .get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP)
                            .getOutputSizes(SurfaceHolder.class)[0].getWidth(),
                    cm.getCameraCharacteristics(cm.getCameraIdList()[0])
                            .get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP)
                            .getOutputSizes(SurfaceHolder.class)[0].getHeight());
            //this is the worst code I have ever written in my entire life
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
        final Surface surface = surfhold.getSurface();
        final List<Surface> list = new ArrayList<Surface>();
        ir.setOnImageAvailableListener(new ImageReader.OnImageAvailableListener() {
            @Override
            public void onImageAvailable(ImageReader imageReader) {
                Image image = imageReader.acquireLatestImage();
                Image.Plane[] planes = image.getPlanes();
                ByteBuffer buffer = planes[0].getBuffer();
                buffer.rewind();
                byte[] data = new byte[buffer.capacity()];
                buffer.get(data);
                Bitmap mybitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                Log.d("ADASD", mybitmap.toString());
                String filename = System.currentTimeMillis() + ".jpg";
                File file = new File(Environment.getExternalStorageDirectory()+"/Instashare");
                if(!file.exists()){
                    file.mkdir();
                }

                try{
                    File finalfile = new File(file, filename);
                    FileOutputStream fos = new FileOutputStream(finalfile);
                    mybitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                    fos.flush();
                    fos.close();

                }catch (Exception e){
                    e.printStackTrace();

                }
                image.close();



            }
        }, sv.getHandler());
        irsurface = ir.getSurface();
        list.add(surface);
        list.add(irsurface);

        doMoreCameraStuff(list);
    }


    public void doMoreCameraStuff(List<Surface> ls) throws CameraAccessException {
        while (true) {
            if (ActivityCompat.checkSelfPermission(a, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                    ActivityCompat.checkSelfPermission(a, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(a, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
                //do something
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
            } else {
                break;
            }
        }

        final List<Surface> lsomething = ls;
        cm.openCamera(cm.getCameraIdList()[0], new CameraDevice.StateCallback() {
            @Override
            public void onOpened(@NonNull CameraDevice cameraDevice) {
                cd = cameraDevice;
                sv.getHolder().addCallback(new SurfaceHolder.Callback() {
                    @Override
                    public void surfaceCreated(SurfaceHolder surfaceHolder) {

                        try {
                            cd.createCaptureSession(lsomething, new CameraCaptureSession.StateCallback() {
                                @Override
                                public void onConfigured(@NonNull CameraCaptureSession cameraCaptureSession) {
                                    ccsession = cameraCaptureSession;
                                    try {
                                        CaptureRequest.Builder cr = cd.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
                                        cr.addTarget(lsomething.get(0));
                                        ccsession.setRepeatingRequest(cr.build(), new CameraCaptureSession.CaptureCallback() {
                                            @Override
                                            public void onCaptureStarted(@NonNull CameraCaptureSession session, @NonNull CaptureRequest request, long timestamp, long frameNumber) {

                                                super.onCaptureStarted(session, request, timestamp, frameNumber);
                                            }

                                        }, sv.getHandler());
                                    } catch (CameraAccessException e) {
                                        e.printStackTrace();
                                    }
                                }

                                @Override
                                public void onConfigureFailed(@NonNull CameraCaptureSession cameraCaptureSession) {

                                }
                            }, sv.getHandler());
                        } catch (CameraAccessException e) {
                            e.printStackTrace();
                        }


                    }

                    @Override
                    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

                    }

                    @Override
                    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {

                    }
                });




            }

            @Override
            public void onDisconnected(@NonNull CameraDevice cameraDevice) {

            }

            @Override
            public void onError(@NonNull CameraDevice cameraDevice, int i) {

            }
        }, sv.getHandler());


    }

    public void endCapture()
    {
        ccsession.close();
        cd.close();
        sv.getHolder().getSurface().release();
        ir.close();

    }


    public void takePictureNow() throws CameraAccessException {
        CaptureRequest.Builder cr = cd.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
        cr.addTarget(irsurface);
        ccsession.capture(cr.build(), new CameraCaptureSession.CaptureCallback() {
            @Override
            public void onCaptureStarted(@NonNull CameraCaptureSession session, @NonNull CaptureRequest request, long timestamp, long frameNumber) {
                super.onCaptureStarted(session, request, timestamp, frameNumber);
            }
        }, sv.getHandler());
    }

}

