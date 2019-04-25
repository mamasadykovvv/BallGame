import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.util.Timer;
import java.util.TimerTask;

public class Ball implements GameConstants {

    private int inAction; 	// Состояние шарика
    private int x;		// координаты по x и y
    private int y;
    private int dx;		//ускорение по осям x и y
    private  int dy;
    private  int radius;	//радиус
    private  int dRadius;	//приращение радиуса
    private Color color;	//цвет
    private static int count;
    public final int id=count++; // идентификатор (номер) шарика
    private static int score; // счёт
    private Timer gameTimer;
    private TimerTask gameTimerTask; //таймер отслеживающий время жизни шарика

    //конструктор Ball
    Ball(int x, int y, int dx, int dy, int radius, Color color, int inAction, int dRadius){
        this.x=x;
        this.y=y;
        this.dx=dx;
        this.dy=dy;
        this.radius=radius;
        this.color=color;
        this.inAction=inAction;
        this.dRadius=dRadius;
        gameTimer = new Timer();
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public int getInAction() {
        return inAction;
    }

    public void setInAction(int inAction) {
        this.inAction = inAction;
    }

    //функция отвечающая за отрисовку шарика
    public Ellipse2D getShape(){
        return new Ellipse2D.Double(x-radius, y-radius, radius*2, radius*2);
    }

    //отслеживание движения и столкновения мячиков:
    public void moveBall(BallComponent ballComponent){
        x+=dx;
        y+=dy;
        radius+=dRadius;
        if(x<=0+radius){
            x=radius;
            dx=-dx;
        }
        if (x>=DEFAULT_WIDTH-radius){
            x=DEFAULT_WIDTH-radius;
            dx=-dx;
        }
        if(y<=0+radius){
            y=radius;
            dy=-dy;
        }
        if (y>=DEFAULT_HEIGHT-radius){
            y=DEFAULT_HEIGHT-radius;
            dy=-dy;
        }
        for(Ball ballVer: ballComponent.listBall){
            //Столкновение - мы пробегаем по массиву содержащему все объекты Ball,
            //и построчно проверяем, не  столкнулся ли «неактивированный» шарик,
            //с проверяемым (ballVer), и в каком состоянии находится проверяемый шар
            //И не является ли он сам собой (для чего и понадобился id)

            if(inAction==0)
                if((Math.sqrt(Math.pow(x-ballVer.x,2)+Math.pow(y-ballVer.y,2)))<=radius+ballVer.radius &&
                        id!=ballVer.id &&
                        (ballVer.inAction==1 || ballVer.inAction==2)) {
                    ballComponent.score++;
                    ballComponent.totalScore++;
                    dx=dy=0;
                    inAction=1;
                    ballComponent.setBackground(ballComponent.getBackground().brighter());
                }

            if(inAction==1){
                dRadius=1;
                if (radius>=MAXRADIUS){
                    inAction=2;
                    dRadius=0;
                    //запускается таймер, который по прошествии времени жизни, начнёт уменьшать радиус шарика
                    gameTimerTask = new gameTimerTask(this);
                    gameTimer.schedule(gameTimerTask, LIFETIME);
                }
            }
            //Если радиус достиг нуля - мы удаляем шарик из списка
            if(inAction==2 && radius<=0){
                ballComponent.listBall.remove(this);
            }}}

    //таймер, запускаемый по истечении LIFETIME, если радиус шарика достиг максимального:
    class gameTimerTask extends TimerTask{

        private Ball ballTimer;

        public gameTimerTask(Ball ball) {
            // TODO Auto-generated constructor stub
            this.ballTimer = ball;
        }
        public void run() {
            // TODO Auto-generated method stub
            ballTimer.dRadius=-1;
        }
    }
}