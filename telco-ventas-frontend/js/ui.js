import { isLoggedIn, getRole, getUsername, clearSession } from "./auth.js";
import { initLogin } from "./pages/login.js";
import { initAgente } from "./pages/agente.js";
import { initBackoffice } from "./pages/backoffice.js";
import { initSupervisor } from "./pages/supervisor.js";
import { initReportes } from "./pages/reportes.js";

const pageLogin = document.getElementById("pageLogin");
const pageApp = document.getElementById("pageApp");
const userInfo = document.getElementById("userInfo");
const btnLogout = document.getElementById("btnLogout");

const tabs = {
  agente: document.getElementById("tabAgente"),
  backoffice: document.getElementById("tabBackoffice"),
  supervisor: document.getElementById("tabSupervisor"),
  reportes: document.getElementById("tabReportes"),
};

const panels = {
  agente: document.getElementById("panelAgente"),
  backoffice: document.getElementById("panelBackoffice"),
  supervisor: document.getElementById("panelSupervisor"),
  reportes: document.getElementById("panelReportes"),
};

function showTab(name) {
  for (const k of Object.keys(tabs)) {
    tabs[k].classList.toggle("active", k === name);
    panels[k].hidden = (k !== name);
  }
}

function applyRoleVisibility(role) {
  Object.values(tabs).forEach(t => t.hidden = true);

  switch(role) {
    case "AGENTE":
      tabs.agente.hidden = false;
      showTab("agente");
      break;

    case "BACKOFFICE":
      tabs.backoffice.hidden = false;
      showTab("backoffice");
      break;

    case "SUPERVISOR":
      tabs.supervisor.hidden = false;
      tabs.reportes.hidden = false;
      showTab("supervisor");
      break;

    case "ADMIN":
      //  admin ve todo, pero no puede acceder a nada (sin autorizacion)
      Object.values(tabs).forEach(t => t.hidden = false);
      showTab("agente");
      break;
  }
}

export function refreshUI() {
  if (!isLoggedIn()) {
    pageLogin.hidden = false;
    pageApp.hidden = true;
    userInfo.textContent = "No autenticado";
    btnLogout.hidden = true;
    return;
  }

  const role = getRole();
  const username = getUsername();

  userInfo.textContent = `${username} · ${role}`;
  btnLogout.hidden = false;

  pageLogin.hidden = true;
  pageApp.hidden = false;

  applyRoleVisibility(role);
}

// Logout limpio y profesional
btnLogout.addEventListener("click", () => {
  clearSession();
  window.location.reload(); // <-- IMPORTANTE
});

tabs.agente?.addEventListener("click", () => showTab("agente"));
tabs.backoffice?.addEventListener("click", () => showTab("backoffice"));
tabs.supervisor?.addEventListener("click", () => showTab("supervisor"));
tabs.reportes?.addEventListener("click", () => showTab("reportes"));

// Inicialización
initLogin(refreshUI);
initAgente();
initBackoffice();
initSupervisor();
initReportes();

refreshUI();