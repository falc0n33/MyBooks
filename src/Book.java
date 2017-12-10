public class Book implements Comparable<Book> {

	private String title;
	private String link;
	private String image;
	private int rate;
	private String comment;

	public Book() {

	}

	public Book(String title, String link, String image, int rate, String comment) {
		this.setTitle(title);
		this.setLink(link);
		this.setImage(image);
		this.setRate(rate);
		this.setComment(comment);
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

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	@Override
	public int compareTo(Book arg) {
		return this.getTitle().compareTo(arg.getTitle());
	}

}
