function postUsingDownloadHiddenForm(url) {
	// Creating a dynamic IFrame instead of static to avoid the security warning
	// in IE when protocol is HTTPS
	createInvisibleIFrame(location.protocol + "//" + location.host);
	var InvisibleIFrame = getElementById("InvisibleIFrame");
	InvisibleIFrame.name = InvisibleIFrame.id;
	var oldFrameName = window.frames[window.frames.length - 1].name;

	// Dynamic IFrames are not added to window.frames hence we forcefully add it
	// so that it can be the target for downloadHiddenForm.
	window.frames[window.frames.length - 1].name = InvisibleIFrame.name;

	// Creating a dynamic form which will post the download request
	var tempForm = document.createElement("form");
	tempForm.setAttribute("id", "downloadHiddenForm");
	tempForm.setAttribute("name", "downloadHiddenForm");
	tempForm.setAttribute("action", url);
	tempForm.setAttribute("target", "InvisibleIFrame");
	tempForm.setAttribute("method", "post");

	var downloadHiddenForm = document.body.appendChild(tempForm);
	downloadHiddenForm.submit();

	// Resetting window.frames to old value since other javaScript might depend
	// on it.
	window.frames[window.frames.length - 1].name = oldFrameName;
}

function validateEmail(string) {
	if (string.length < 1)
		return true;

	if (string
			.search(/^\w+((-\w+)|(\.\w+))*\@[A-Za-z0-9]+((\.|-)[A-Za-z0-9]+)*\.[A-Za-z0-9]+$/) != -1)
		return true;
	else
		return false;
}

function removeParameterFromUrl(url, param) {
	if (url.length == 0 || param.length == 0)
		return url;

	var idx = url.indexOf(param);
	if (idx == -1)
		return url;

	var idx2 = url.indexOf("&", idx);
	var ret = url.substring(0, idx - 1);
	if (idx2 != -1)
		ret = ret + url.substring(idx2);
	return ret;
}

var englishLetters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

// Raw ascii characters cause IE to crash when HTML window is built and opened
// at runtime.
// Note: exclude xF7 since that's the divide symbol.
// var tempLetters =
// "?????????????????????????????????????????????????????????????";

var otherLetters = "\xC0\xC1\xC2\xC3\xC4\xC5\xC6\xC7\xC8\xC9\xCA\xCB\xCC\xCD\xCE\xCF";
otherLetters += "\xD0\xD1\xD2\xD3\xD4\xD5\xD6\xD7\xD8\xD9\xDA\xDB\xDC\xDD\xDE\xDF";
otherLetters += "\xE0\xE1\xE2\xE3\xE4\xE5\xE6\xE7\xE8\xE9\xE0\xEA\xEB\xEC\xED\xEE\xEF";
otherLetters += "\xF0\xF1\xF2\xF3\xF4\xF5\xF6\xF8\xF9\xF0\xFA\xFB\xFC\xFD\xFE\xFF";

var allLetters = englishLetters + otherLetters;

function isalpha(c, allowNonEnglish) {
	if (englishLetters.indexOf(c) != -1)
		return true;
	if (allowNonEnglish && isNonEnglishCharacter(c))
		return true;

}

function isNonEnglishCharacter(c) {
	if (otherLetters.indexOf(c) != -1)
		return true;
	// test for Unicode
	var s = new String(c);
	var charCode = s.charCodeAt(0);
	if (charCode > 256)
		return true;
	return false;
}

var allDigits = "0123456789";
function isdigit(c) {
	return allDigits.indexOf(c) != -1;
}

function isalnum(c) {
	return isalpha(c) || isdigit(c);
}

function isalnumOrNonEnglish(c) {
	return isalpha(c) || isdigit(c) || isNonEnglishCharacter(c);
}

function stringCompare(str1, str2, isCaseInsensitive) {
	str1 = replaceAll(str1, '\\\\', '\\\\');
	var regExp;
	if (isCaseInsensitive)
		regExp = new RegExp(str1, "i");
	else {
		regExp = new RegExp(str1);
	}
	if (str2.match(regExp) == null)
		return false;

	return true;
}

function replaceAll(str, searchTerm, replaceWith) {
	var regex = "/" + searchTerm + "/g";
	return str.replace(eval(regex), replaceWith);
}

// Cookie code liberated from JavaScript.com

// name - name of the cookie
// value - value of the cookie
// [expires] - expiration date of the cookie (defaults to end of current
// session)
// [path] - path for which the cookie is valid (defaults to path of calling
// document)
// [domain] - domain for which the cookie is valid (defaults to domain of
// calling document)
// [secure] - Boolean value indicating if the cookie transmission requires a
// secure transmission
// * an argument defaults when it is assigned null as a placeholder
// * a null placeholder is not required for trailing omitted arguments
function setCookie(name, value, expires, path, domain, secure) {
	var curCookie = name + "=" + encodeURIComponent(value)
			+ ((expires) ? "; expires=" + expires.toGMTString() : "")
			+ ((path) ? "; path=" + path : "")
			+ ((domain) ? "; domain=" + domain : "")
			+ ((secure) ? "; secure" : "");
	document.cookie = curCookie;
}

function setLongLivedCookie(name, value) {
	var expires = new Date(new Date().getTime() + 2419200000);
	setCookie(name, value, expires);
}

// name - name of the desired cookie
// * return string containing value of specified cookie or null if cookie does
// not exist
function getCookie(name) {
	var dc = document.cookie;
	var prefix = name + "=";
	var begin = dc.indexOf("; " + prefix);
	if (begin == -1) {
		begin = dc.indexOf(prefix);
		if (begin != 0)
			return null;
	} else
		begin += 2;
	var end = document.cookie.indexOf(";", begin);
	if (end == -1)
		end = dc.length;
	return decodeURIComponent(dc.substring(begin + prefix.length, end));
}

function cookieIsSet(name) {
	var value = getCookie(name);
	if (!value)
		return false;
	if (value.toLowerCase() == "false")
		return false;
	return true;
}

// name - name of the cookie
// [path] - path of the cookie (must be same as path used to create cookie)
// [domain] - domain of the cookie (must be same as domain used to create
// cookie)
// * path and domain default if assigned null or omitted if no explicit argument
// proceeds
function deleteCookie(name, path, domain) {
	if (getCookie(name)) {
		document.cookie = name + "=" + ((path) ? "; path=" + path : "")
				+ ((domain) ? "; domain=" + domain : "")
				+ "; expires=Thu, 01-Jan-70 00:00:01 GMT";
	}
}
