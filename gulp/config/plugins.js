import browsersync from "browser-sync";
import replace from "gulp-replace";
import newer from "gulp-newer";

export const plugins = {
	replace: replace,
	newer: newer,
	browsersync: browsersync
}