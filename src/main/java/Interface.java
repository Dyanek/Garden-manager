import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import java.awt.*;

public class Interface extends JFrame {

    public Interface(){
        JFrame frame = new JFrame("Jardin Vertical");

        // Setting the width and height of frame

        // Setting the width and height of frame
        // 1.get the screen size as a java dimension
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        // 2.get 2/3 of the height, and 2/3 of the width
        int height = screenSize.height * 4 / 5;
        int width = screenSize.width * 4 / 5;
        System.out.println(height + " - " +width);
        // 3.set the jframe height and width
        frame.setSize(width, height);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


        JPanel panel1 = new JPanel();
        panel1.setSize(width/4,height/4);
        JLabel label1 = new JLabel("Panel 1");
        panel1.add(label1);

        JPanel panel2 = new JPanel();
        panel2.setSize(width/4,height/4);
        JLabel label2 = new JLabel("Panel 2");
        panel1.add(label2);

        JPanel panel3 = new JPanel();
        panel3.setSize(width/4,height/4);
        JLabel label3 = new JLabel("Panel 3");
        panel1.add(label3);

        JPanel panel4 = new JPanel();
        panel4.setSize(width/4,height/4);
        JLabel label4 = new JLabel("Panel 4");
        panel1.add(label4);

        // add components
        frame.add(panel1);
        frame.add(panel2);
        frame.add(panel3);
        frame.add(panel4);
        frame.setLayout(new GridLayout(2,2));

        // set window visible and center
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        Interface myInterface = new Interface();
        // myInterface.update();
    }
    /*public void update(){
        repaint();
    }
    public void paint(Graphics g) {
        g.setColor(Color.black);
        //有角矩形，起始点(10,30)，宽80，高50
        g.drawRect(10,30,80,50);
        //圆角矩形，起始点(110,30)，宽80，高50，角（a=20,b=10）
        g.drawRoundRect(110,30,80,50,20,10);
        //椭圆，圆心（110,90）、a=80,b=50
        g.drawOval(110,90,80,50);
        //一条弧，圆心（219,30）、a=80,b=50 角度在0-90之间
        g.drawArc(210,30,80,50,0,90);
        //扇面，圆心（219,90）、a=80,b=50 角度在0-90之间
        g.fillArc(210,90,80,50,0,90);
    }*/

};



    /*private static void placeComponents(JPanel panel) {

        panel.setLayout(null);


        JLabel userLabel = new JLabel("User:");

        userLabel.setBounds(10,20,80,25);
        panel.add(userLabel);

        *//*
         * 创建文本域用于用户输入
         *//*
        JTextField userText = new JTextField(20);
        userText.setBounds(100,20,165,25);
        panel.add(userText);

        // 输入密码的文本域
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setBounds(10,50,80,25);
        panel.add(passwordLabel);

        *//*
         *这个类似用于输入的文本域
         * 但是输入的信息会以点号代替，用于包含密码的安全性
         *//*
        JPasswordField passwordText = new JPasswordField(20);
        passwordText.setBounds(100,50,165,25);
        panel.add(passwordText);

        // 创建登录按钮
        JButton loginButton = new JButton("login");
        loginButton.setBounds(10, 80, 80, 25);
        panel.add(loginButton);
    }*/

