import javax.swing.*;
import java.io.*;

class LibraryManagement
{
    static String filename = "library.txt";

    public static void main(String args[])
    {
        JFrame jf = new JFrame("Library Management");
        jf.setSize(500, 500);
        jf.setLayout(null);
        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JLabel title = new JLabel("LIBRARY MANAGEMENT SYSTEM");
        title.setBounds(100, 10, 300, 30);
        jf.add(title);

        JLabel l1 = new JLabel("Book ID:");
        l1.setBounds(30, 50, 100, 30);
        jf.add(l1);

        JTextField bookid = new JTextField();
        bookid.setBounds(140, 50, 150, 30);
        jf.add(bookid);

        JLabel l2 = new JLabel("Member Name:");
        l2.setBounds(30, 90, 100, 30);
        jf.add(l2);

        JTextField member = new JTextField();
        member.setBounds(140, 90, 150, 30);
        jf.add(member);

        JLabel l3 = new JLabel("Issue Date:");
        l3.setBounds(30, 135, 100, 30);
        jf.add(l3);

        JComboBox<Integer> idate = new JComboBox<>();
        JComboBox<Integer> imonth = new JComboBox<>();
        JComboBox<Integer> iyear = new JComboBox<>();

        for (int i = 1; i <= 31; i++)
            idate.addItem(i);

        for (int i = 1; i <= 12; i++)
            imonth.addItem(i);

        for (int i = 2020; i <= 2035; i++)
            iyear.addItem(i);

        idate.setBounds(140, 135, 45, 25);
        imonth.setBounds(190, 135, 45, 25);
        iyear.setBounds(240, 135, 65, 25);

        jf.add(idate);
        jf.add(imonth);
        jf.add(iyear);

        JLabel l4 = new JLabel("Return Date:");
        l4.setBounds(30, 175, 100, 30);
        jf.add(l4);

        JComboBox<Integer> rdate = new JComboBox<>();
        JComboBox<Integer> rmonth = new JComboBox<>();
        JComboBox<Integer> ryear = new JComboBox<>();

        for (int i = 1; i <= 31; i++)
            rdate.addItem(i);

        for (int i = 1; i <= 12; i++)
            rmonth.addItem(i);

        for (int i = 2020; i <= 2035; i++)
            ryear.addItem(i);

        rdate.setBounds(140, 175, 45, 25);
        rmonth.setBounds(190, 175, 45, 25);
        ryear.setBounds(240, 175, 65, 25);

        jf.add(rdate);
        jf.add(rmonth);
        jf.add(ryear);

        JButton issue = new JButton("Issue");
        issue.setBounds(30, 220, 90, 30);
        jf.add(issue);

        JButton ret = new JButton("Return");
        ret.setBounds(130, 220, 90, 30);
        jf.add(ret);

        JButton view = new JButton("View Records");
        view.setBounds(230, 220, 130, 30);
        jf.add(view);

        JTextArea ta = new JTextArea();
        ta.setBounds(30, 270, 420, 160);
        jf.add(ta);

        issue.addActionListener(e ->
        {
            try
            {
                String id = bookid.getText();
                String name = member.getText();

                String dob = idate.getSelectedItem() + "-"
                           + imonth.getSelectedItem() + "-"
                           + iyear.getSelectedItem();

                if (id.equals("") || name.equals(""))
                {
                    System.out.println("Enter Book ID and Member Name.");
                }
                else
                {
                    FileWriter fw = new FileWriter(filename, true);
                    BufferedWriter bw = new BufferedWriter(fw);

                    bw.write("ISSUE#" + id + "#" + name + "#" + dob);
                    bw.newLine();
                    bw.close();

                    JOptionPane.showMessageDialog(jf,
                        "Book issued successfully.");
                }
            }
            catch (IOException ex)
            {
                System.out.println("File error.");
            }
        });

        ret.addActionListener(e ->
        {
            try
            {
                String id = bookid.getText();

                String issueDate = idate.getSelectedItem() + "-"
                                 + imonth.getSelectedItem() + "-"
                                 + iyear.getSelectedItem();

                String returnDate = rdate.getSelectedItem() + "-"
                                  + rmonth.getSelectedItem() + "-"
                                  + ryear.getSelectedItem();

                if (id.equals(""))
                {
                    System.out.println("Enter Book ID.");
                }
                else
                {
                    String issued[] = findIssuedBook(id);

                    issueDate = issued[3];

                    int i = convertDate(issueDate);
                    int r = convertDate(returnDate);

                    if (r < i)
                    {
                        throw new InvalidDateException();
                    }

                    int days = r - i;
                    int fine = 0;

                    if (days > 7)
                    {
                        fine = (days - 7) * 5;
                    }

                    FileWriter fw = new FileWriter(filename, true);
                    BufferedWriter bw = new BufferedWriter(fw);

                    bw.write("RETURN#" + id + "#" + issued[2] + "#"
                           + issueDate + "#" + returnDate + "#" + fine);
                    bw.newLine();
                    bw.close();

                    JOptionPane.showMessageDialog(jf,
                        "Book returned successfully.\n"
                        + "Borrowing days: " + days + "\n"
                        + "Fine: Rs. " + fine);
                }
            }
            catch (InvalidDateException ex)
            {
                System.out.println("Invalid date.");
            }
            catch (RecordNotFoundException ex)
            {
                System.out.println("Record not found.");
            }
            catch (IOException ex)
            {
                System.out.println("File error.");
            }
        });

        view.addActionListener(e ->
        {
            try
            {
                BufferedReader br = new BufferedReader(
                    new FileReader(filename));

                String line;
                ta.setText("");

                while ((line = br.readLine()) != null)
                {
                    String a[] = line.split("#");

                    if (a[0].equals("ISSUE"))
                    {
                        ta.append("ISSUE RECORD\n");
                        ta.append("Book ID: " + a[1] + "\n");
                        ta.append("Member Name: " + a[2] + "\n");
                        ta.append("Issue Date: " + a[3] + "\n");
                    }
                    else if (a[0].equals("RETURN"))
                    {
                        ta.append("RETURN RECORD\n");
                        ta.append("Book ID: " + a[1] + "\n");
                        ta.append("Member Name: " + a[2] + "\n");
                        ta.append("Issue Date: " + a[3] + "\n");
                        ta.append("Return Date: " + a[4] + "\n");
                        ta.append("Fine: Rs. " + a[5] + "\n");
                    }

                    ta.append("----------------------\n");
                }

                br.close();
            }
            catch (IOException ex)
            {
                System.out.println("No records found.");
            }
        });

        jf.setVisible(true);
    }

    static int convertDate(String date)
    {
        String a[] = date.split("-");

        int day = Integer.parseInt(a[0]);
        int month = Integer.parseInt(a[1]);
        int year = Integer.parseInt(a[2]);

        return year * 365 + month * 30 + day;
    }

    static String[] findIssuedBook(String id)
            throws RecordNotFoundException, IOException
    {
        BufferedReader br;
        String line;
        String issued[] = null;

        try
        {
            br = new BufferedReader(new FileReader(filename));

            while ((line = br.readLine()) != null)
            {
                String a[] = line.split("#");

                if (a.length >= 2 && a[1].equals(id))
                {
                    if (a[0].equals("ISSUE"))
                    {
                        issued = a;
                    }
                    else if (a[0].equals("RETURN"))
                    {
                        issued = null;
                    }
                }
            }

            br.close();
        }
        catch (FileNotFoundException e)
        {
            throw new RecordNotFoundException();
        }

        if (issued == null)
        {
            throw new RecordNotFoundException();
        }

        return issued;
    }
}

class InvalidDateException extends Exception
{
}

class RecordNotFoundException extends Exception
{
}