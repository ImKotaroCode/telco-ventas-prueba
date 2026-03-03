import { API_BASE } from "./config.js";
import { getToken } from "./auth.js";

async function request(path, { method="GET", body=null, auth=true } = {}) {
  const headers = { "Content-Type": "application/json" };
  if (auth) {
    const token = getToken();
    if (token) headers["Authorization"] = `Bearer ${token}`;
  }

  const res = await fetch(`${API_BASE}${path}`, {
    method,
    headers,
    body: body ? JSON.stringify(body) : null
  });

  const text = await res.text();
  let data = null;
  try { data = text ? JSON.parse(text) : null; } catch { data = { raw: text }; }

  if (!res.ok) {
    const err = new Error(data?.message || res.statusText);
    err.status = res.status;
    err.data = data;
    throw err;
  }
  return data;
}

export const api = {
  login: (payload) => request(`/auth/login`, { method:"POST", body: payload, auth:false }),

  // Agente
  crearVenta: (payload) => request(`/ventas`, { method:"POST", body: payload }),
  misVentas: (query) => request(`/ventas/mis-ventas${query}`),

  // Backoffice
  pendientes: (query) => request(`/ventas/pendientes${query}`),
  aprobar: (id) => request(`/ventas/${id}/aprobar`, { method:"POST" }),
  rechazar: (id, motivoRechazo) => request(`/ventas/${id}/rechazar`, { method:"POST", body: { motivoRechazo } }),

  // Supervisor
  equipo: (query) => request(`/ventas/equipo${query}`),

  // Reportes
  resumen: (query) => request(`/reportes/resumen${query}`)
};