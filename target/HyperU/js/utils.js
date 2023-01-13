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

function formatDateTime(date) {
  dateTime = new Date(date);

  return dateTime.toLocaleString();
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

function setCookie(name, value, expiration) {
  var d = new Date();
  d.setTime(d.getTime() + expiration * 24 * 60 * 60 * 1000);
  var expires = "expires=" + d.toUTCString();
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

async function checkLogin(isLoginPage = false) {
  var jwt = sessionStorage.getItem("jwt");

  var parsedJwt = parseJwt(jwt);
  var loggedIn = false;

  if (parsedJwt != null && new Date().getTime() < parsedJwt.exp * 1000) {
    loggedIn = true;
  }

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

  if (!loggedIn) {
    var mustLogin = true;
    var loginCookie = getCookie("token");
    if (loginCookie != null && loginCookie.trim() != "") {
      var userID = loginCookie.split(".")[0];
      var token = loginCookie.split(".")[1];

      try {
        await $.ajax({
          url: contextPath + "user/token",
          method: "POST",
          data: $.param({ id: userID, token: token }),
          contentType: "application/x-www-form-urlencoded; charset=UTF-8",
          success: function (data) {
            var userData = data.data;
            var userToken = userData.jwtoken;
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
    if (mustLogin && !isLoginPage) {
      location.href = baseUri + "login";
    }
  } else {
    if (isLoginPage) {
      location.href = baseUri + "feed";
    }
  }

  $("#splashScreen").remove();
}

function toHashtag(tag) {
  hashtag = tag.toLowerCase();
  hashtag = hashtag.replaceAll(" ", "-");
  return hashtag;
}

function getUrlTokens(tokenStart) {
  tokenStart = "/" + tokenStart + "/";
  var locationHref = location.href;
  if (locationHref.indexOf("#") >= 0) {
    locationHref = locationHref.substr(0, locationHref.indexOf("#"));
  }
  return locationHref.substr(locationHref.indexOf(tokenStart) + tokenStart.length).split("/");
}

function createToast(toastMessage, toastType) {
  var closeClass = toastType == "warning" ? "btn-close" : "btn-close-white";
  var textClass = toastType == "warning" ? "text-dark" : "text-white";

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

function processMessage(messageText) {
  messageText = messageText.trim().replace(/(?:\r\n|\r|\n)/g, "<br>");
  return messageText;
}

function deprocessMessage(messageText) {
  messageText = messageText.replace(/<br\s*[\/]?>/gi, "\n").trim();
  return messageText;
}

function showError(data) {
  if (data !== undefined) {
    data = data.responseJSON;
    createToast(data.message.message, "danger");
  }
}

function showSuccess(data) {
  if (data !== undefined) {
    createToast(data.message.message, "success");
  }
}
