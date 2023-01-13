// Check if cookies accepted, otherwise show notification
var cookieAccepted = getCookie("cookie");
if (cookieAccepted == null) {
  var cookieBanner = $(`<div class="toast bg-light text-dark" role="alert" aria-live="assertive" aria-atomic="true" data-bs-autohide="false">
    <div class="toast-body">
      We use cookies to improve your experience. <a href="${baseUri + "privacy"}">Read more.</a>
      <div class="mt-2 pt-2 border-top">
        <button type="button" class="btn btn-primary btn-sm" id="acceptCookies">
          Accept
        </button>
      </div>
    </div>
  </div>`);

  cookieBanner.find("#acceptCookies").click(function () {
    setCookie("cookie", "accepted");
    $(this).parents(".toast").remove();
  });

  $("#notifications").prepend(cookieBanner);

  var toast = new bootstrap.Toast($("#notifications .toast").get(0));

  toast.show();
}

let deferredPrompt;

window.addEventListener("beforeinstallprompt", (e) => {
  e.preventDefault();

  deferredPrompt = e;

  if (getCookie("installpwa") == null || getCookie("installpwa") != "no") {
    $("#installPWAModal").modal("show");
  }
});

$("#installPWAButton").click(async () => {
  $("#installPWAModal").modal("hide");

  deferredPrompt.prompt();
  await deferredPrompt.userChoice;
  deferredPrompt = null;
});

$("#notInstallPWAButton").click(function () {
  setCookie("installpwa", "no");
});

window.addEventListener("appinstalled", () => {
  $("#installPWAModal").modal("hide");
  deferredPrompt = null;
});
