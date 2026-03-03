import { api } from "../api.js";

function isoFromDatetimeLocal(value) {
  if (!value) return "";
  return value.length === 16 ? `${value}:00` : value;
}
function money(v){
  const n = Number(v ?? 0);
  return n.toLocaleString("es-PE", { minimumFractionDigits: 2, maximumFractionDigits: 2 });
}

export function initReportes() {
  const rDesde = document.getElementById("rDesde");
  const rHasta = document.getElementById("rHasta");
  const btn = document.getElementById("btnResumen");
  const msg = document.getElementById("msgReportes");

  const kPend = document.getElementById("kPend");
  const kAprob = document.getElementById("kAprob");
  const kRech = document.getElementById("kRech");
  const kMonto = document.getElementById("kMonto");

  const tbody = document.getElementById("tbodySerie");

  async function cargarResumen() {
    msg.hidden = true;
    tbody.innerHTML = "";

    const desde = isoFromDatetimeLocal(rDesde.value);
    const hasta = isoFromDatetimeLocal(rHasta.value);

    const params = new URLSearchParams();
    if (desde) params.set("desde", desde);
    if (hasta) params.set("hasta", hasta);

    try {
      const res = await api.resumen(`?${params.toString()}`);

      // ✅ TU BACKEND:
      // { conteosPorEstado: {PENDIENTE:1,...}, montoTotalAprobadas: 159.90, ventasPorDia:[{fecha:"YYYY-MM-DD", count:1, monto:0}] }

      const conteos = res.conteosPorEstado || {};
      kPend.textContent = String(conteos.PENDIENTE ?? 0);
      kAprob.textContent = String(conteos.APROBADA ?? 0);
      kRech.textContent = String(conteos.RECHAZADA ?? 0);

      kMonto.textContent = money(res.montoTotalAprobadas ?? 0);

      const serie = res.ventasPorDia || [];
      for (const it of serie) {
        const tr = document.createElement("tr");
        tr.innerHTML = `
          <td>${it.fecha ?? ""}</td>
          <td>${it.count ?? 0}</td>
          <td>${money(it.monto ?? 0)}</td>
        `;
        tbody.appendChild(tr);
      }

      if (!serie.length) {
        const tr = document.createElement("tr");
        tr.innerHTML = `<td colspan="3" class="muted">Sin datos en el rango</td>`;
        tbody.appendChild(tr);
      }
    } catch (err) {
      msg.className = "msg err";
      msg.textContent = `${err.status || ""} ${err.message}`;
      msg.hidden = false;
    }
  }

  btn.addEventListener("click", () => cargarResumen());

  // Por defecto: últimos 14 días
  const now = new Date();
  const past = new Date(now.getTime() - 14 * 24 * 60 * 60 * 1000);
  rHasta.value = now.toISOString().slice(0, 16);
  rDesde.value = past.toISOString().slice(0, 16);
}