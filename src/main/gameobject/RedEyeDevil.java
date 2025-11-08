/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.gameobject;

import main.state.GameWorldState;
import main.effect.Animation;
import main.effect.CacheDataLoader;
// XÓA: import java.applet.AudioClip;
import java.awt.Graphics2D;
import java.awt.Rectangle;

/**
 *
 * @author phamn
 */
public class RedEyeDevil extends ParticularObject {

    private Animation forwardAnim, backAnim;
    
    private long startTimeToShoot;
    
    // XÓA KHAI BÁO BIẾN ÂM THANH
    // private AudioClip shooting; 
    
    public RedEyeDevil(float x, float y, GameWorldState gameWorld) {
        super(x, y, 127, 89, 0, 100, gameWorld);
        backAnim = CacheDataLoader.getInstance().getAnimation("redeye");
        forwardAnim = CacheDataLoader.getInstance().getAnimation("redeye");
        forwardAnim.flipAllImage();
        startTimeToShoot = 0;
        setDamage(10);
        setTimeForNoBehurt(300000000);
        // XÓA KHỞI TẠO ÂM THANH
        // shooting = CacheDataLoader.getInstance().getSound("redeyeshooting"); 
    }

    @Override
    public void attack() {
    
        // XÓA PHÁT ÂM THANH
        // shooting.play(); 
        
        Bullet bullet = new RedEyeBullet(getPosX(), getPosY(), getGameWorld());
        if(getDirection() == LEFT_DIR) bullet.setSpeedX(-8);
        else bullet.setSpeedX(8);
        bullet.setTeamType(getTeamType());
        getGameWorld().bulletManager.addObject(bullet);
    
    }

    
    public void Update(){
        super.Update();
        
        // Tối ưu hóa: Cập nhật Animation trong Update() thay vì Draw()
        if(getDirection() == LEFT_DIR){
            backAnim.Update(System.nanoTime());
        }else{
            forwardAnim.Update(System.nanoTime());
        }
        
        if(System.nanoTime() - startTimeToShoot > 1000*10000000){
            attack();
            System.out.println("Red Eye attack");
            startTimeToShoot = System.nanoTime();
        }
    }
    
    @Override
    public Rectangle getBoundForCollisionWithEnemy() {
        Rectangle rect = getBoundForCollisionWithMap();
        rect.x += 20;
        rect.width -= 40;
        
        return rect;
    }

    @Override
    public void draw(Graphics2D g2) {
        if(!isObjectOutOfCameraView()){
            if(getState() == NOBEHURT && (System.nanoTime()/10000000)%2!=1){
                // plash...
            }else{
                if(getDirection() == LEFT_DIR){
                    // XÓA: backAnim.Update(System.nanoTime());
                    backAnim.draw((int) (getPosX() - getGameWorld().camera.getPosX()), 
                            (int)(getPosY() - getGameWorld().camera.getPosY()), g2);
                }else{
                    // XÓA: forwardAnim.Update(System.nanoTime());
                    forwardAnim.draw((int) (getPosX() - getGameWorld().camera.getPosX()), 
                            (int)(getPosY() - getGameWorld().camera.getPosY()), g2);
                }
            }
        }
        //drawBoundForCollisionWithEnemy(g2);
    }
    
}