import { api } from "../api.js";

function isoFromDatetimeLocal(value) {
  if (!value) return "";
  return value.length === 16 ? `${value}:00` : value;
}

export function initSupervisor() {
  const tbody = document.getElementById("tbodyEquipo");
  const msg = document.getElementById("msgSupervisor");

  const fEstado = document.getElementById("fEstadoEq");
  const fAgente = document.getElementById("fAgenteIdEq");
  const fDesde = document.getElementById("fDesdeEq");
  const fHasta = document.getElementById("fHastaEq");
  const btn = document.getElementById("btnBuscarEq");

  async function cargarEquipo() {
    msg.hidden = true;
    tbody.innerHTML = "";

    const params = new URLSearchParams();
    params.set("page", "0");
    params.set("size", "20");
    params.set("sort", "fechaRegistro");
    params.set("dir", "DESC");

    if (fEstado.value) params.set("estado", fEstado.value);
    if (fAgente.value) params.set("agenteId", fAgente.value);

    const desde = isoFromDatetimeLocal(fDesde.value);
    const hasta = isoFromDatetimeLocal(fHasta.value);
    if (desde) params.set("desde", desde);
    if (hasta) params.set("hasta", hasta);

    try {
      const res = await api.equipo(`?${params.toString()}`);
      for (const v of (res.content || [])) {
        const tr = document.createElement("tr");
        tr.innerHTML = `
          <td>${v.id}</td>
          <td>${v.agenteUsername || v.agenteId || ""}</td>
          <td>${v.codigoLlamada || ""}</td>
          <td>${(v.nombreCliente || "").replaceAll("<","&lt;")}</td>
          <td>${v.monto ?? ""}</td>
          <td>${v.estado || ""}</td>
          <td>${v.fechaRegistro || ""}</td>
        `;
        tbody.appendChild(tr);
      }

      if (!res.content?.length) {
        msg.className = "msg";
        msg.textContent = "Sin resultados para tu equipo";
        msg.hidden = false;
      }
    } catch (err) {
      msg.className = "msg err";
      msg.textContent = `${err.status || ""} ${err.message}`;
      msg.hidden = false;
    }
  }

  btn.addEventListener("click", () => cargarEquipo());
  setTimeout(() => cargarEquipo().catch(()=>{}), 600);
}