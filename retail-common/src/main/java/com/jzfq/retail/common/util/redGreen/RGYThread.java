package com.jzfq.retail.common.util.redGreen;

public class RGYThread{

    public RGYThread() {
        while(true){
            System.out.println("东西-->红灯，南北-->绿灯  ");
            for(int i=10;i>0;i--){
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if(i<=3){
                    System.out.print("东西-->红灯，南北-->黄灯  ");
                    System.err.print(i+" ");
                }else{
                    System.err.print(i+" ");
                }
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println();
            System.out.println();
            System.out.println("东西-->绿灯，南北-->红灯  ");
            for(int i=10;i>0;i--){
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if(i<=3){
                    System.out.print("东西-->黄灯，南北-->红灯  ");
                    System.err.print(i+" ");
                }else{
                    System.err.print(i+" ");
                }
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println();
            System.out.println();
        }
    }

}
