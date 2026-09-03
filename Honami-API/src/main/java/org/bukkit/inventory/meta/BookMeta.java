package org.bukkit.inventory.meta;

import java.util.List;

import org.bukkit.Material;

public interface BookMeta extends ItemMeta {

	boolean hasTitle();

	String getTitle();

	boolean setTitle(String title);

	boolean hasAuthor();

	String getAuthor();

	void setAuthor(String author);

	boolean hasPages();

	String getPage(int page);

	void setPage(int page, String data);

	List<String> getPages();

	void setPages(List<String> pages);

	void setPages(String... pages);

	void addPage(String... pages);

	int getPageCount();

	BookMeta clone();
}
