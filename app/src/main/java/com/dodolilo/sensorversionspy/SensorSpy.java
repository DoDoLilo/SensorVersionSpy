package com.dodolilo.sensorversionspy;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 获取手机上的各种传感器、传感器型号信息的类.
 */
public class SensorSpy {
    private final SensorManager sensorManager;
    private final Context context;
    private final List<Sensor> deviceSensorList;

    SensorSpy(Context outContext) {
        context = outContext;
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        deviceSensorList = sensorManager.getSensorList(Sensor.TYPE_ALL);
    }

    /**
     * 向外提供Sensor List的拷贝.
     *
     * @return {@link SensorSpy#deviceSensorList} 的拷贝
     */
    public List<Sensor> getDeviceSensorList() {
        return new ArrayList<>(deviceSensorList);
    }

    /**
     * 向外提供传感器的名字和数量.
     *
     * @return Map<传感器name, 数量number>
     */
    public Map<String, Integer> getDeviceSensorNameList() {
        Map<String, Integer> nameMap = new HashMap<>(deviceSensorList.size());
        for (Sensor sensor : deviceSensorList) {
            nameMap.put(sensor.getName(), nameMap.getOrDefault(sensor.getName(), 0) + 1);
        }
        return nameMap;
    }

    public String getAllSensorInf() {
        StringBuilder sensorInf = new StringBuilder("名称,制造商,版本\n");

        for (Sensor sensor : deviceSensorList) {
            sensorInf.append(sensor.getName() + "," + sensor.getVendor() + "," + sensor.getVersion());
            sensorInf.append("\n");
        }

        return sensorInf.toString();
    }


    public enum SensorType {
        ACCELEROMETER(Sensor.TYPE_ACCELEROMETER),
        GYROSCOPE(Sensor.TYPE_GYROSCOPE),
        MAGNETOMETER(Sensor.TYPE_MAGNETIC_FIELD);

        private final int typeId;

        SensorType(int typeId) {
            this.typeId = typeId;
        }

        public int getTypeId() {
            return typeId;
        }
    }
}
