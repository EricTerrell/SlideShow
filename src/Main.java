/*
  SlideShow
  (C) Copyright 2021, Eric Bergman-Terrell

  This file is part of SlideShow.

    SlideShow is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    SlideShow is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with SlideShow.  If not, see <http://www.gnu.org/licenses/>.
*/

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import static java.lang.System.exit;

public class Main {
    private Runnable runnable;

    private Image image;

    private Main(Display display, List<Path> allImagePaths, Canvas canvas, int secondsPerImage) {
        final Random random = new Random();

        canvas.addPaintListener(e -> {
            final Rectangle canvasBounds = canvas.getBounds();
            final Rectangle imageBounds = image.getBounds();

            final Point margin =
                    new Point((canvasBounds.width - imageBounds.width) / 2,
                              (canvasBounds.height - imageBounds.height) / 2);

            e.gc.drawImage(image, margin.x, margin.y);
        });

        runnable = () -> {
            // Hide the cursor by moving it off of the screen.
            display.setCursorLocation(display.getClientArea().width, display.getClientArea().height);

            try {
                if (image != null && !image.isDisposed()) {
                    image.dispose();
                    image = null;
                }

                image = new Image(display, allImagePaths.get(random.nextInt(allImagePaths.size())).toFile().getAbsolutePath());

                canvas.redraw();
            } catch (Exception ex) {
                System.out.println(ex);

                // If this attempt failed, try the next image immediately.
                display.timerExec(0, runnable);
            }

            display.timerExec(secondsPerImage * 1000, runnable);
        };
    }

    private void run() {
        runnable.run();
    }

    public static void main(String[] args) {
        if (args.length < 3) {
            System.out.println("SlideShow {image folder} {seconds per image} {file type 1} ... {file type N}\r\n");
            System.out.println("For example:\r\n");
            System.out.println("SlideShow \"D:\\photos\" 10 \".jpg\" \".jpeg\" \".png\"\r\n");
            exit(0);
        }

        final String imagesFolder = args[0];
        final int secondsPerImage = Integer.parseInt(args[1]);

        final List<String> fileTypes = new ArrayList<>();

        for (int i = 2; i < args.length; i++) {
            fileTypes.add(args[i].toUpperCase());
        }

        System.out.println("SlideShow v. 1.00\r\n");
        
        System.out.printf("Searching for files in %s\r\n%n", args[0]);

        System.out.println("File types:\r\n");

        fileTypes.forEach(System.out::println);

        System.out.println();

        List<Path> allImagePaths = new ArrayList<>();

        try {
            allImagePaths = Files.walk(Paths.get(imagesFolder))
                    .filter(path -> path.toFile().isFile())
                    .filter(path -> isPictureFile(path, fileTypes))
                    .collect(Collectors.toList());
        } catch (Exception ex) {
            System.out.println(ex);
        }

        System.out.printf("Loaded %,d files\r\n%n", allImagePaths.size());

        Display display = Display.getDefault();

        System.out.printf("Image size should be %d x %d\r\n%n", display.getBounds().width, display.getBounds().height);

        System.out.println("Press Esc to stop\r\n");

        if (allImagePaths.size() > 0) {
            try {
                Thread.sleep(1000 * 10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // Make sure that no other windows cover up the app's window.
            final Shell shell = new Shell(display, SWT.NO_TRIM | SWT.ON_TOP);

            shell.setBounds(display.getBounds());

            final GridLayout gridLayout = new GridLayout(1, false);
            gridLayout.marginHeight = gridLayout.marginWidth = 0;
            shell.setLayout(gridLayout);

            final Canvas canvas = new Canvas(shell, SWT.NO_TRIM);
            GridData gridData = new GridData(SWT.FILL);
            gridData.grabExcessHorizontalSpace = true;
            gridData.grabExcessVerticalSpace = true;
            gridData.horizontalAlignment = SWT.FILL;
            gridData.verticalAlignment = SWT.FILL;
            canvas.setLayoutData(gridData);

            canvas.setBackground(display.getSystemColor(SWT.COLOR_BLACK));

            // Allow the user to stop the program by typing Esc.
            canvas.addKeyListener(new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent e) {
                    if (e.keyCode == SWT.ESC) {
                        display.dispose();
                    }
                }

                @Override
                public void keyReleased(KeyEvent e) {
                }
            });

            shell.open();

            new Main(display, allImagePaths, canvas, secondsPerImage).run();

            while (!shell.isDisposed()) {
                if (!display.readAndDispatch()) {
                    display.sleep();
                }
            }

            display.dispose();
        } else {
            exit(0);
        }
    }

    private static boolean isPictureFile(Path path, List<String> fileTypes) {
        final String fileName = path.toFile().getName().toUpperCase();

        return fileTypes.stream().anyMatch(fileName::endsWith);
    }
}
