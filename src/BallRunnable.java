import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Random;

class BallRunnable implements Runnable, GameConstants{
    private BallComponent ballComponent;
    private JLabel scoreLabel;
    private int level, ballQnt;
    private MousePlayer mousePlayerListener;
    private int goal;

    public BallRunnable(final BallComponent ballComponent, JLabel scoreLabel, int level, int ballQnt) {

        this.ballComponent = ballComponent;
        this.scoreLabel = scoreLabel;
        this.level=level;
        this.ballQnt=ballQnt;
        this.goal=2;
    }

    class MousePlayer extends MouseAdapter {

        public void mousePressed(MouseEvent e) {
            Random random = new Random();
            Ball ball = new Ball(e.getX(),
                    e.getY(),
                    0,
                    0,
                    BASERADIUS,
                    new Color(random.nextInt(255),random.nextInt(255),random.nextInt(255)),
                    1,
                    1);
            ballComponent.addBall(ball);
            ballComponent.startClick=true;
            ballComponent.removeMouseListener(mousePlayerListener);
            ballComponent.removeMouseMotionListener(mousePlayerListener);
            ballComponent.setCursor(Cursor.getDefaultCursor());
        }}
    public void run(){
        while(true){
            try{
                mousePlayerListener = new MousePlayer();
                ballComponent.addMouseListener(mousePlayerListener);
                ballComponent.addMouseMotionListener(mousePlayerListener);

                //меняем внешний вид курсора на крестик
                ballComponent.setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));

                //сколько осталось шариков в работе
                int countInWork=1;

                // Генерация массива шариков
                //приращения скорости задаются случайно
                //приращение радиуса равно нулю
                for (int i=0;i<ballQnt; i++){
                    Random randomX = new Random();
                    Random randomY = new Random();
                    Ball ball = new Ball(randomX.nextInt(DEFAULT_WIDTH),
                            randomY.nextInt(DEFAULT_HEIGHT),
                            randomX.nextInt(2)+1,
                            randomY.nextInt(2)+1,
                            BASERADIUS,
                            new Color(randomX.nextInt(255),randomX.nextInt(255),randomX.nextInt(255)),
                            0,
                            0);
                    ballComponent.addBall(ball);
                }

                // пока есть активированные шарики
                while (countInWork!=0){
                    countInWork=0;
                    if(!ballComponent.startClick) {
                        EventQueue.invokeLater(new Runnable() {
                                                   public void run() {
                                                       // TODO Auto-generated method stub
                                                       scoreLabel.setText("Цель: выбить "+ goal+" шаров из "+ ballQnt);
                                                   }
                                               }
                        );
                        countInWork=1;
                    }
                    for(Ball ball: ballComponent.listBall){
                        if((ball.getInAction()==1 || ball.getInAction()==2)) countInWork++; //если остались активированные шарики
                        ball.moveBall(ballComponent);
                        ballComponent.repaint();
                        if(ballComponent.startClick){
                            //обновляем информационную строку
                            EventQueue.invokeLater(new Runnable() {
                                public void run() {
                                    scoreLabel.setText("Уровень: "+ level+", Вы выбили "+ballComponent.score+" из "+ballQnt);
                                }});
                        }}
                    Thread.sleep(DELAY);
                }
            } catch (InterruptedException ex){
                ex.printStackTrace();
            }
            ballComponent.listBall.clear();
            ballComponent.repaint();
            //Выводим результат
            if(ballComponent.score<goal) {
                EventQueue.invokeLater(new Runnable() {
                    public void run() {
                        scoreLabel.setText("Цель уровня не достигнута!");
                    }
                });
                JOptionPane.showMessageDialog(ballComponent,
                        "Цель уровня не достигнута. \nНабрано очков: "+
                                ballComponent.totalScore+".\n Попробуйте еще раз.");
                ballComponent.startClick=false;
                ballComponent.score=0;
                ballComponent.setBackground(Color.DARK_GRAY);
            }
            else{
                EventQueue.invokeLater(new Runnable() {
                    public void run() {
                        scoreLabel.setText("Уровень пройден!!!");
                    }
                });
                ballComponent.startClick=false;
                level++;
                ballQnt++;
                goal++;
                ballComponent.setBackground(Color.DARK_GRAY);
                ballComponent.score=0;
                JOptionPane.showMessageDialog(ballComponent, "Уровень "+level+".\nЦель: выбить "+ goal+" шаров из "+ ballQnt);
            }}}}