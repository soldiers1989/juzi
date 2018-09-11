package com.jzfq.retail.common.util.redGreen;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class TestThread extends JFrame{
    private Thread thread1;
    private Thread thread2;
    private Thread thread3;
    final Lock l = new ReentrantLock();
    private static int state = 0;

    public TestThread() {
        //红灯亮5秒
        thread1 = new Thread(new Runnable() {
            @Override
            public void run() {
                while(state>-1) {
                    l.lock();
                    if(state%3==0) {
                        try {
                            Thread.sleep(200);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        Graphics graphics =getGraphics();
                        Graphics2D graphics2d = (Graphics2D) graphics;
                        Shape shape = new Ellipse2D.Double(30,60,100,100);
                        graphics.setColor(Color.red);
                        graphics2d.fill(shape);

                        try {
                            Thread.sleep(5000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        graphics.clearRect(30, 60, 100, 100);
                        state++;
                    }

                    l.unlock();
                }

            }
        });
        //黄灯亮2秒
        thread2 = new Thread(new Runnable() {
            @Override
            public void run() {
                while(state>-1) {
                    l.lock();
                    if(state%3==1) {
                        try {
                            Thread.sleep(0);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        Graphics graphics =getGraphics();
                        Graphics2D graphics2d = (Graphics2D) graphics;
                        Shape shape1 = new Ellipse2D.Double(180,60,100,100);
                        graphics.setColor(Color.yellow);
                        graphics2d.fill(shape1);

                        try {
                            Thread.sleep(2000);//等待2秒
                        } catch (InterruptedException e) {
                            // TODO 自动生成的 catch 块
                            e.printStackTrace();
                        }
                        graphics.clearRect(180, 60, 100, 100);//清除
                        state++;
                    }
                    l.unlock();
                }

            }
        });
        //路灯亮4秒
        thread3 = new Thread(new Runnable() {
            @Override
            public void run() {
                while(state>-1) {
                    l.lock();
                    if(state%3==2) {
                        try {
                            Thread.sleep(0);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        Graphics graphics =getGraphics();
                        Graphics2D graphics2d = (Graphics2D) graphics;
                        Shape shape2 = new Ellipse2D.Double(330,60,100,100);//设置圆的位置
                        graphics.setColor(Color.green);//颜色
                        graphics2d.fill(shape2);//填充

                        try {
                            Thread.sleep(3500);

                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        graphics.clearRect(330, 60, 100, 100);    //清除圆
                        state++;
                    }
                    l.unlock();
                }
            }
        });
        //启动线程
        thread1.start();
        thread2.start();
        thread3.start();

    }
    public static void init(JFrame jFrame,int width,int height) {
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//窗口关闭方式
        jFrame.setSize(width, height);//窗口大小
        jFrame.setVisible(true);//可见
    }

    public static void main(String[] args) {
        init(new TestThread(), 500, 200);
    }

}
