import { api } from "../api.js";

export function initBackoffice() {
  const tbody = document.getElementById("tbodyPendientes");
  const msg = document.getElementById("msgBackoffice");
  const btnRef = document.getElementById("btnRefrescarPendientes");

  async function cargarPendientes() {
    msg.hidden = true;
    tbody.innerHTML = "";
    try {
      const params = new URLSearchParams({ page:"0", size:"20", sort:"fechaRegistro", dir:"ASC" });
      const res = await api.pendientes(`?${params.toString()}`);
      for (const v of (res.content || [])) {
        const tr = document.createElement("tr");
        tr.innerHTML = `
          <td>${v.id}</td>
          <td>${v.agenteUsername || v.agenteId || ""}</td>
          <td>${v.codigoLlamada || ""}</td>
          <td>${(v.nombreCliente || "").replaceAll("<","&lt;")}</td>
          <td>${v.monto ?? ""}</td>
          <td class="actions">
            <button class="btn btn-ok" data-act="aprobar" data-id="${v.id}">Aprobar</button>
            <button class="btn btn-danger" data-act="rechazar" data-id="${v.id}">Rechazar</button>
          </td>
        `;
        tbody.appendChild(tr);
      }
      if (!res.content?.length) {
        msg.className = "msg";
        msg.textContent = "No hay pendientes";
        msg.hidden = false;
      }
    } catch (err) {
      msg.className = "msg err";
      msg.textContent = `${err.status || ""} ${err.message}`;
      msg.hidden = false;
    }
  }

  tbody.addEventListener("click", async (e) => {
    const btn = e.target.closest("button");
    if (!btn) return;
    const id = btn.dataset.id;
    const act = btn.dataset.act;

    try {
      if (act === "aprobar") {
        await api.aprobar(id);
      } else if (act === "rechazar") {
        const motivo = prompt("Motivo de rechazo (obligatorio):");
        if (!motivo || !motivo.trim()) return;
        await api.rechazar(id, motivo.trim());
      }
      await cargarPendientes();
    } catch (err) {
      msg.className = "msg err";
      msg.textContent = `${err.status || ""} ${err.message}`;
      msg.hidden = false;
    }
  });

  btnRef.addEventListener("click", () => cargarPendientes());

  setTimeout(() => cargarPendientes().catch(()=>{}), 500);
}