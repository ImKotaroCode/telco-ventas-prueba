import { api } from "../api.js";
import { setSession } from "../auth.js";

export function initLogin(onLogged) {
  const form = document.getElementById("loginForm");
  const msg = document.getElementById("loginMsg");

  form.addEventListener("submit", async (e) => {
    e.preventDefault();
    msg.hidden = true;

    const fd = new FormData(form);
    const username = fd.get("username")?.toString().trim();
    const password = fd.get("password")?.toString();

    try {
      const res = await api.login({ username, password });

      const token = res.token || res.jwt || res.accessToken;
      const rol = res.rol || res.role;
      const user = res.username || username;

      if (!token || !rol) throw new Error("Respuesta de login sin token/rol. Ajusta el mapping en login.js");

      setSession({ token, username: user, rol });
      msg.className = "msg ok";
      msg.textContent = "Login OK";
      msg.hidden = false;

      onLogged?.();
    } catch (err) {
      msg.className = "msg err";
      msg.textContent = `${err.status || ""} ${err.message}`;
      msg.hidden = false;
    }
  });
}