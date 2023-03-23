package me.darksidecode.bfu.books.ui;

import lombok.SneakyThrows;
import net.miginfocom.swing.MigLayout;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class AboutTab extends JPanel {

    @SneakyThrows
    public AboutTab() {
        setLayout(new MigLayout());

        var lblTitle = new JLabel("BFU - Databases Lab. No. 8");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        add(lblTitle, "wrap");
        
        var lblSubtitle = new JLabel("The \"Books\" Information System");
        lblSubtitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        add(lblSubtitle, "wrap");
        
        var lblAuthorName = new JLabel("Author: German Vekhorev");
        lblAuthorName.setFont(UiOptions.genericFont);
        add(lblAuthorName, "wrap");
        
        var lblAuthorPos = new JLabel("IKBFU, Applied Mathematics and Computer Science, 3rd year");
        lblAuthorPos.setFont(UiOptions.genericFont);
        add(lblAuthorPos, "wrap");
        
        var lblYear = new JLabel("2023");
        lblYear.setFont(UiOptions.genericFont);
        add(lblYear, "wrap");

        var capyImage = ImageIO
                .read(Objects.requireNonNull(getClass().getClassLoader().getResource("capy.jpg")))
                .getScaledInstance(350, 325, Image.SCALE_SMOOTH);
        var lblCapy = new JLabel(new ImageIcon(capyImage));
        add(lblCapy);
    }

}
