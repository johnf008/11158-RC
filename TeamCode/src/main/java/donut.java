
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import java.util.Arrays;

@TeleOp(name="donut", group="Testing")
public class donut extends OpMode {
    int k;
    double A = 0, B = 0, i, j;
    double[] z = new double[1760]; // array of doubles to store depth values at each point
    char[] b = new char[1760]; // characters in the donut (ASCII)


    @Override
    public void init() {



    }

    @Override
    public void loop() {
        for (; ; ) {
            telemetry.clearAll();
            Arrays.fill(b, 0, 1760, ' '); // Fill the arrays with initial values
            Arrays.fill(z, 0, 1760, 0);

            /*
                Nested loops to iterate over each point of the donut
                j = rotation around y-axis
                i = rotation around x-axis
                c, d, e, f, g = sine and cosine values for angles of rotation
                h = distance between viewpoint and donut
                D = depth value for current point
                l, m, n = sine and cosine values for viewpoint angles of rotations
                t = used to compute point's position
            */
            for (j = 0; 6.28 > j; j += 0.07)
                for (i = 0; 6.28 > i; i += 0.02) {
                    double c = Math.sin(i),
                            d = Math.cos(j),
                            e = Math.sin(A),
                            f = Math.sin(j),
                            g = Math.cos(A),
                            h = d + 2,
                            D = 1 / (c * h * e + f * g + 5),
                            l = Math.cos(i),
                            m = Math.cos(B),
                            n = Math.sin(B),
                            t = c * h * g - f * e;

                    /*
                        x, y = coordinates of pixel relative to top-left of terminal window
                        z, b = arrays for depth and character representation at
                        o = index of the pixel
                        N = character that represents current pixel
                     */

                    int x = (int) (40 + 30 * D * (l * h * m - t * n)),
                            y = (int) (12 + 15 * D * (l * h * n + t * m)),
                            o = x + 80 * y,
                            N = (int) (8 * ((f * e - c * d * g) * m - c * d * e - f * g - l * d * n));

                    /*
                        If current pixel is within terminal window
                        And if closer to viewpoint than previously drawn pixel
                        Then update z[o] and b[o] with new depth and therefor new ASCII character
                     */
                    if (22 > y && y > 0 && x > 0 && 80 > x && D > z[o]) {
                        z[o] = D;
                        b[o] = new char[]{'.', ',', '-', '~', ':', ';', '=', '!', '*', '#', '$', '@'}[Math.max(N, 0)];
                    }
                }

            System.out.print("\u001b[H"); // ANSI escape code "Home" - move cursor to top-left of window


            for (int row = 0; row < 22; row++) {
                String line = new String(b, row * 80, 80);
                telemetry.addLine(line);
            }

            A += 0.01;
            B += 0.005;
            telemetry.update();

        }

    }


}