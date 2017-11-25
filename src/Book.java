public class Book implements Comparable<Book>{

    private String title;
    private String link;
    private String image;
    private int rate;
    
    public Book() {
    	
    }
    
    public Book(String title, String link, String image, int rate){
        this.setTitle(title);
        this.setLink(link);
        this.setImage(image);
        this.setRate(rate);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
    
    public int getRate() {
    	return rate;
    }
    
    public void setRate(int rate) {
    	this.rate = rate;
    }

	@Override
	public int compareTo(Book arg) {
		return this.getTitle().compareTo(arg.getTitle());
	}
    
}
