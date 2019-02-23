package instashare.instashare;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Camera;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CameraHandler {


    CameraCaptureSession ccsession;
    SurfaceView sv;
    final CameraManager cm;
    Activity a;
    CameraDevice cd;




    public CameraHandler(SurfaceView sv, Activity a) throws CameraAccessException {
        this.a = a;
        this.sv = sv;
        cm = (CameraManager) a.getSystemService(Context.CAMERA_SERVICE);
        final SurfaceView finalview = sv;
         SurfaceHolder surfhold = finalview.getHolder();
        surfhold.setKeepScreenOn(true);
        try {
            surfhold.setFixedSize(cm.getCameraCharacteristics(cm.getCameraIdList()[0])
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
        final List<Surface> list = Arrays.asList(surface);
        doMoreCameraStuff(list);
    }


    public void doMoreCameraStuff(List<Surface> ls) throws CameraAccessException {
        while (true) {
            if (ActivityCompat.checkSelfPermission(a, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(a, new String[]{Manifest.permission.CAMERA}, 0);
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

}

