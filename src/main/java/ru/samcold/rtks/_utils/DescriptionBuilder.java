package ru.samcold.rtks._utils;

import padeg.lib.Padeg;

public class DescriptionBuilder {
    private String work;
    private String crane;

    public DescriptionBuilder(String work, String crane) {
        this.work = work;
        this.crane = crane;
    }

    public String getWork() {
        return work;
    }

    public void setWork(String work) {
        this.work = work;
    }

    public void setCrane(String crane) {
        this.crane = crane;
    }

    public String getDescription() {
        String result = "";
        result += work;
        if (!crane.isEmpty()) {
            String[] arr = crane.split(" ");
            StringBuilder craneOut = new StringBuilder();
            for (String c : arr) {
                c = Padeg.getAppointmentPadeg(c, 2);
                craneOut.append(c).append(" ");
            }
            result += " " + firstToLower(craneOut.toString()).trim();
        }
        return result;
    }

    private static String firstToLower(String s) {
        return s.substring(0, 0) + Character.toLowerCase(s.charAt(0)) + s.substring(1);
    }

}
