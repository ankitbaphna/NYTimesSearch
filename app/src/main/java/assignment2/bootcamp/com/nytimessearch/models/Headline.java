
package assignment2.bootcamp.com.nytimessearch.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class Headline {

    @SerializedName("main")
    @Expose
    private String main;
    @SerializedName("content_kicker")
    @Expose
    private String contentKicker;
    @SerializedName("kicker")
    @Expose
    private String kicker;
    @SerializedName("print_headline")
    @Expose
    private String printHeadline;

    /**
     * 
     * @return
     *     The main
     */
    public String getMain() {
        return main;
    }

    /**
     * 
     * @param main
     *     The main
     */
    public void setMain(String main) {
        this.main = main;
    }

    /**
     * 
     * @return
     *     The contentKicker
     */
    public String getContentKicker() {
        return contentKicker;
    }

    /**
     * 
     * @param contentKicker
     *     The content_kicker
     */
    public void setContentKicker(String contentKicker) {
        this.contentKicker = contentKicker;
    }

    /**
     * 
     * @return
     *     The kicker
     */
    public String getKicker() {
        return kicker;
    }

    /**
     * 
     * @param kicker
     *     The kicker
     */
    public void setKicker(String kicker) {
        this.kicker = kicker;
    }

    /**
     * 
     * @return
     *     The printHeadline
     */
    public String getPrintHeadline() {
        return printHeadline;
    }

    /**
     * 
     * @param printHeadline
     *     The print_headline
     */
    public void setPrintHeadline(String printHeadline) {
        this.printHeadline = printHeadline;
    }

}
