//Global variables that are used in almost every page
const contextPath = "https://hyperu.space/api/";
const baseUri = "https://hyperu.space/";

const loadingSpinner = $(`<div class="d-flex align-items-center justify-content-center my-3" id="loadingSpinner">
                            <div class="spinner-border text-primary" role="status">
                              <span class="visually-hidden">Loading...</span>
                            </div>
                          </div>`);

function beforeSend(xhr) {
  // var userToken = sessionStorage.getItem("jwt");
  // xhr.setRequestHeader("Authorization", "Bearer " + userToken);
}

//Various functions to format date and time according to different standards required
function formatDateTime(date) {
  date = new Date(date);

  return formatDate(date) + ", " + formatTime(date);
}

function formatTime(date) {
  date = new Date(date);

  return date.getHours().toString().padStart(2, "0") + ":" + date.getMinutes().toString().padStart(2, "0");
}

function formatDate(date) {
  date = new Date(date);
  return date.getDate().toString().padStart(2, "0") + "/" + (date.getMonth() + 1).toString().padStart(2, "0") + "/" + date.getFullYear();
}

function formatDateText(date) {
  date = new Date(date);

  const months = ["January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"];

  return date.getDate() + " " + months[date.getMonth()] + " " + date.getFullYear();
}

function onlyDate(date) {
  date = new Date(date);
  return new Date(date.getFullYear(), date.getMonth(), date.getDate());
}

function calculateAge(birthDate) {
  var differenceMs = Date.now() - birthDate.getTime();
  var ageDifference = new Date(differenceMs);

  return Math.abs(ageDifference.getUTCFullYear() - 1970);
}

//Functions to manage cookies

function setCookie(name, value, expiration = 0) {
  var d = new Date();
  if (expiration > 0) {
    d.setTime(d.getTime() + expiration * 24 * 60 * 60 * 1000);
    var expires = "expires=" + d.toUTCString();
  }
  document.cookie = name + "=" + value + ";" + expires + ";path=/";
}

function getCookie(name) {
  var name = name + "=";
  var ca = document.cookie.split(";");
  for (var i = 0; i < ca.length; i++) {
    var c = ca[i];
    while (c.charAt(0) == " ") {
      c = c.substring(1);
    }
    if (c.indexOf(name) == 0) {
      return c.substring(name.length, c.length);
    }
  }
  return null;
}

function deleteCookie(name) {
  expires = "expires=-1";
  document.cookie = name + "=;" + expires + ";path=/";
}

//Function to parse JWT TOKEN
function parseJwt(token) {
  if (token == null || token.trim() == "") {
    return null;
  }
  var base64Url = token.split(".")[1];
  var base64 = base64Url.replace(/-/g, "+").replace(/_/g, "/");
  var jsonPayload = decodeURIComponent(
    atob(base64)
      .split("")
      .map(function (c) {
        return "%" + ("00" + c.charCodeAt(0).toString(16)).slice(-2);
      })
      .join("")
  );

  return JSON.parse(jsonPayload);
}

//Function to check if user is already logged
async function checkLogin(isLoginPage = false) {
  const jwt = sessionStorage.getItem("jwt");

  const parsedJwt = parseJwt(jwt);
  var loggedIn = false;

  // There is a valid JWT in sessionStorage
  if (parsedJwt != null && new Date().getTime() < parsedJwt.exp * 1000) {
    loggedIn = true;
  }

  // If is logged in by JWT, check if there is a cookie session, otherwise, login
  // In normal way, it would be enough to pass the JWT with Bearer Authentication in the beforeSend operation,
  // but since the images are loaded from the browser, a JSESSION cookie is needed.
  if (loggedIn) {
    try {
      await $.ajax({
        url: contextPath + "user/login",
        method: "GET",
      });
    } catch (error) {
      loggedIn = false;
    }
  }

  // If not logged, auto-login with token, if present
  if (!loggedIn) {
    var mustLogin = true;
    var loginCookie = getCookie("token");

    // If there is a login token, use it to auto login
    if (loginCookie != null && loginCookie.trim() != "") {
      const userID = loginCookie.split(".")[0];
      const token = loginCookie.split(".")[1];

      try {
        await $.ajax({
          url: contextPath + "user/token",
          method: "POST",
          data: $.param({ id: userID, token: token }),
          contentType: "application/x-www-form-urlencoded; charset=UTF-8",
          success: function (data) {
            var userData = data.data;
            var userToken = userData.jwtoken;

            // Set the jwt, set the new auto-login token, and reload the page
            sessionStorage.setItem("jwt", userToken);
            setCookie("token", userData.user.id + "." + userData.token);
            mustLogin = false;
            location.reload();
          },
        });
      } catch (error) {
        mustLogin = true;
      }
    }

    // If must login and you're not in login page, redirect to login page, otherwise remove splashscreen
    if (mustLogin && !isLoginPage) {
      location.href = baseUri + "login";
    } else {
      $("#splashScreen").fadeOut(300, function () {
        $(this).remove();
      });
    }
  } else {
    // If already logged, if you're in login page redirect to feed, otherwise simply remove the spashscreen
    if (isLoginPage) {
      location.href = baseUri + "feed";
    } else {
      $("#splashScreen").fadeOut(300, function () {
        $(this).remove();
      });
    }
  }
}

//Function to transform words into hastags
function toHashtag(tag) {
  hashtag = tag.toLowerCase();
  hashtag = hashtag.replace(/\s+/g, "-");
  return hashtag;
}

//Function to get tokens from URLs
function getUrlTokens(tokenStart) {
  tokenStart = "/" + tokenStart + "/";
  var locationHref = location.href;
  if (locationHref.indexOf("#") >= 0) {
    locationHref = locationHref.substr(0, locationHref.indexOf("#"));
  }
  return locationHref.substr(locationHref.indexOf(tokenStart) + tokenStart.length).split("/");
}

//Function to displat a Toast
function createToast(toastMessage, toastType) {
  const closeClass = toastType == "warning" ? "btn-close" : "btn-close-white";
  const textClass = toastType == "warning" ? "text-dark" : "text-white";

  var toastElement = $(`<div class="toast align-items-center ${textClass} bg-${toastType} border-0" role="alert" aria-live="assertive" aria-atomic="true">
                          <div class="d-flex">
                            <div class="toast-body">${toastMessage}</div>
                            <button type="button" class="btn-close ${closeClass} me-2 m-auto" data-bs-dismiss="toast" aria-label="Close"></button>
                          </div>
                        </div>`);

  $("#notifications").prepend(toastElement);

  var toast = new bootstrap.Toast($("#notifications .toast").get(0));

  toast.show();
}

//Function to retrieve the logged user
function getLoggedUser() {
  return parseJwt(sessionStorage.getItem("jwt"));
}

//Functions to transform "new line" into <br> and viceversa
function nl2br(text) {
  text = text.trim().replace(/(?:\r\n|\r|\n)/g, "<br>");
  return text;
}

function br2nl(text) {
  text = text.replace(/<br\s*[\/]?>/gi, "\n").trim();
  return text;
}

//Function to display error messages
function showError(data) {
  if (data !== undefined) {
    data = data.responseJSON;
    createToast(data.message.message, "danger");
  }
}

//Function to display success messages
function showSuccess(data) {
  if (data !== undefined) {
    createToast(data.message.message, "success");
  }
}

//Function to display error for no ideas
function createAstroError(text, subtext) {
  return $(`<div><div class="d-block text-center w-100">
              <img src="${baseUri}/media/astro.svg" class="msg-img w-75" alt="${text}">
              <h2 class="mt-5">${text}</h2>
              <h4>${subtext}</h4>
            </div></div>`);
}

//Manage description and messages
function processDescription(description) {
  return description != undefined && description != null ? marked(description.trim(), { breaks: true }) : "";
}

function processMessage(messageText) {
  return messageText != undefined && messageText != null ? nl2br(messageText) : "";
}

function deprocessMessage(messageText) {
  return messageText != undefined && messageText != null ? br2nl(messageText) : "";
}

if ("serviceWorker" in navigator) {
  navigator.serviceWorker.register(baseUri + "pwa.js");
}

//function to copy a link
function copyToClipboard(text) {
  var tempInput = $("<input>");
  $("body").append(tempInput);
  tempInput.val(text).select();
  document.execCommand("copy");
  tempInput.remove();
}

//Function to check if element is present into an array
function isNotIn(otherArray) {
  return function (current) {
    return (
      otherArray.filter(function (other) {
        return other.id == current.id;
      }).length == 0
    );
  };
}

// Retrieve logged user
var loggedUser = getLoggedUser();
