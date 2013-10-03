package com.ifmo.gosugdrTeam.lesson3;

import java.util.Random;

public class DemoRequest extends Request {
    String[] source = new String[]{
            "https://pp.vk.me/c309730/v309730097/a2e8/WUyixQbURuU.jpg",
            "https://pp.vk.me/c424327/v424327097/1b7d/xrlari6zg0E.jpg",
            "https://pp.vk.me/c309730/v309730097/a3f9/D6mIXlnXqd4.jpg",
            "https://pp.vk.me/c424327/v424327097/1c13/qzwxLNWJN6Q.jpg"};
    static Random random = new Random();

    public DemoRequest(String arg) {
        query = arg;
        translation = "�����";
        images = new String[10];
        for (int i = 0; i < 10; i++) {
            images[i] = source[random.nextInt(4)];
        }
    }
}
