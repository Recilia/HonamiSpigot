package org.github.paperspigot;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

import org.bukkit.entity.Player;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;

public final class Title {

	public static final int DEFAULT_FADE_IN = 20;

	public static final int DEFAULT_STAY = 200;

	public static final int DEFAULT_FADE_OUT = 20;

	private final BaseComponent[] title;
	private final BaseComponent[] subtitle;
	private final int fadeIn;
	private final int stay;
	private final int fadeOut;

	public Title(BaseComponent title) {
		this(title, null);
	}

	public Title(BaseComponent[] title) {
		this(title, null);
	}

	public Title(String title) {
		this(title, null);
	}

	public Title(BaseComponent title, BaseComponent subtitle) {
		this(title, subtitle, DEFAULT_FADE_IN, DEFAULT_STAY, DEFAULT_FADE_OUT);
	}

	public Title(BaseComponent[] title, BaseComponent[] subtitle) {
		this(title, subtitle, DEFAULT_FADE_IN, DEFAULT_STAY, DEFAULT_FADE_OUT);
	}

	public Title(String title, String subtitle) {
		this(title, subtitle, DEFAULT_FADE_IN, DEFAULT_STAY, DEFAULT_FADE_OUT);
	}

	public Title(BaseComponent title, BaseComponent subtitle, int fadeIn, int stay, int fadeOut) {
		this(new BaseComponent[] { checkNotNull(title, "title") },
				subtitle == null ? null : new BaseComponent[] { subtitle }, fadeIn, stay, fadeOut);
	}

	public Title(BaseComponent[] title, BaseComponent[] subtitle, int fadeIn, int stay, int fadeOut) {
		checkArgument(fadeIn >= 0, "Negative fadeIn: %s", fadeIn);
		checkArgument(stay >= 0, "Negative stay: %s", stay);
		checkArgument(fadeOut >= 0, "Negative fadeOut: %s", fadeOut);
		this.title = checkNotNull(title, "title");
		this.subtitle = subtitle;
		this.fadeIn = fadeIn;
		this.stay = stay;
		this.fadeOut = fadeOut;
	}

	public Title(String title, String subtitle, int fadeIn, int stay, int fadeOut) {
		this(TextComponent.fromLegacyText(checkNotNull(title, "title")),
				subtitle == null ? null : TextComponent.fromLegacyText(subtitle), fadeIn, stay, fadeOut);
	}

	public BaseComponent[] getTitle() {
		return this.title;
	}

	public BaseComponent[] getSubtitle() {
		return this.subtitle;
	}

	public int getFadeIn() {
		return this.fadeIn;
	}

	public int getStay() {
		return this.stay;
	}

	public int getFadeOut() {
		return this.fadeOut;
	}

	public static Builder builder() {
		return new Builder();
	}

	public static final class Builder {

		private BaseComponent[] title;
		private BaseComponent[] subtitle;
		private int fadeIn = DEFAULT_FADE_IN;
		private int stay = DEFAULT_STAY;
		private int fadeOut = DEFAULT_FADE_OUT;

		public Builder title(BaseComponent title) {
			return this.title(new BaseComponent[] { checkNotNull(title, "title") });
		}

		public Builder title(BaseComponent[] title) {
			this.title = checkNotNull(title, "title");
			return this;
		}

		public Builder title(String title) {
			return this.title(TextComponent.fromLegacyText(checkNotNull(title, "title")));
		}

		public Builder subtitle(BaseComponent subtitle) {
			return this.subtitle(subtitle == null ? null : new BaseComponent[] { subtitle });
		}

		public Builder subtitle(BaseComponent[] subtitle) {
			this.subtitle = subtitle;
			return this;
		}

		public Builder subtitle(String subtitle) {
			return this.subtitle(subtitle == null ? null : TextComponent.fromLegacyText(subtitle));
		}

		public Builder fadeIn(int fadeIn) {
			checkArgument(fadeIn >= 0, "Negative fadeIn: %s", fadeIn);
			this.fadeIn = fadeIn;
			return this;
		}

		public Builder stay(int stay) {
			checkArgument(stay >= 0, "Negative stay: %s", stay);
			this.stay = stay;
			return this;
		}

		public Builder fadeOut(int fadeOut) {
			checkArgument(fadeOut >= 0, "Negative fadeOut: %s", fadeOut);
			this.fadeOut = fadeOut;
			return this;
		}

		public Title build() {
			checkState(title != null, "Title not specified");
			return new Title(this.title, this.subtitle, this.fadeIn, this.stay, this.fadeOut);
		}
	}
}