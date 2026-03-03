import { api } from "../api.js";

let page = 0;
const size = 10;

function isoFromDatetimeLocal(value) {
  if (!value) return "";
  // datetime-local: "2026-03-03T10:00" => "2026-03-03T10:00:00"
  return value.length === 16 ? `${value}:00` : value;
}

function esc(s) {
  return String(s ?? "").replaceAll("<", "&lt;").replaceAll(">", "&gt;");
}

export function initAgente() {
  // ===== Registrar venta =====
  const form = document.getElementById("formCrearVenta");
  const msgCrear = document.getElementById("msgCrearVenta");
  const btnCrear = form.querySelector('button[type="submit"]');

  const inputDni = form.querySelector('input[name="dniCliente"]');
  const inputTel = form.querySelector('input[name="telefonoCliente"]');

  // mensajes debajo de inputs (sin tocar HTML)
  const dniMsg = document.createElement("div");
  dniMsg.className = "validation-msg";
  inputDni.parentElement.appendChild(dniMsg);

  const telMsg = document.createElement("div");
  telMsg.className = "validation-msg";
  inputTel.parentElement.appendChild(telMsg);

  function onlyDigits(el, maxLen) {
    el.addEventListener("input", () => {
      el.value = el.value.replace(/\D/g, "").slice(0, maxLen);
    });
  }
  onlyDigits(inputDni, 11);
  onlyDigits(inputTel, 9);

  function validateDni() {
    const v = inputDni.value.trim();
    if (v.length === 0) {
      inputDni.classList.remove("valid", "invalid");
      dniMsg.textContent = "";
      return false;
    }
    if (v.length === 8) {
      inputDni.classList.add("valid"); inputDni.classList.remove("invalid");
      dniMsg.textContent = "DNI válido";
      dniMsg.className = "validation-msg ok";
      return true;
    }
    if (v.length === 11) {
      inputDni.classList.add("valid"); inputDni.classList.remove("invalid");
      dniMsg.textContent = "RUC válido";
      dniMsg.className = "validation-msg ok";
      return true;
    }
    inputDni.classList.add("invalid"); inputDni.classList.remove("valid");
    dniMsg.textContent = "Debe tener 8 (DNI) o 11 (RUC) dígitos";
    dniMsg.className = "validation-msg err";
    return false;
  }

  function validateTel() {
    const v = inputTel.value.trim();
    if (v.length === 0) {
      inputTel.classList.remove("valid", "invalid");
      telMsg.textContent = "";
      return false;
    }
    if (v.length === 9) {
      inputTel.classList.add("valid"); inputTel.classList.remove("invalid");
      telMsg.textContent = "Teléfono válido";
      telMsg.className = "validation-msg ok";
      return true;
    }
    inputTel.classList.add("invalid"); inputTel.classList.remove("valid");
    telMsg.textContent = "Debe tener 9 dígitos";
    telMsg.className = "validation-msg err";
    return false;
  }

  function validateRequired() {
    // solo campos del form de crear
    const fd = new FormData(form);
    const payload = Object.fromEntries(fd.entries());

    // trim básicos
    const required = [
      payload.dniCliente,
      payload.nombreCliente,
      payload.telefonoCliente,
      payload.direccionCliente,
      payload.planActual,
      payload.planNuevo,
      payload.codigoLlamada,
      payload.producto,
      payload.monto
    ];

    return required.every(v => String(v ?? "").trim() !== "");
  }

  function validateAllForm() {
    const reqOk = validateRequired();
    const dniOk = validateDni();
    const telOk = validateTel();

    const monto = Number(form.querySelector('input[name="monto"]').value);
    const montoOk = monto > 0;

    btnCrear.disabled = !(reqOk && dniOk && telOk && montoOk);
  }

  form.addEventListener("input", validateAllForm);
  inputDni.addEventListener("input", validateAllForm);
  inputTel.addEventListener("input", validateAllForm);

  // ===== Mis ventas =====
  const tbodyMis = document.getElementById("tbodyMis");
  const msgMis = document.getElementById("msgMis");
  const lblPage = document.getElementById("lblPageMis");
  const btnPrevMis = document.getElementById("btnPrevMis");
  const btnNextMis = document.getElementById("btnNextMis");
  const btnBuscarMis = document.getElementById("btnBuscarMis");

  const fEstadoMis = document.getElementById("fEstadoMis");
  const fDesdeMis = document.getElementById("fDesdeMis");
  const fHastaMis = document.getElementById("fHastaMis");

  async function cargarMisVentas() {
    msgMis.hidden = true;
    tbodyMis.innerHTML = "";
    lblPage.textContent = String(page);

    const params = new URLSearchParams();
    params.set("page", String(page));
    params.set("size", String(size));
    params.set("sort", "fechaRegistro");
    params.set("dir", "DESC");

    if (fEstadoMis.value) params.set("estado", fEstadoMis.value);

    const desde = isoFromDatetimeLocal(fDesdeMis.value);
    const hasta = isoFromDatetimeLocal(fHastaMis.value);
    if (desde) params.set("desde", desde);
    if (hasta) params.set("hasta", hasta);

    try {
      const res = await api.misVentas(`?${params.toString()}`);

      for (const v of (res.content || [])) {
        const tr = document.createElement("tr");
        tr.innerHTML = `
          <td>${v.id}</td>
          <td>${esc(v.codigoLlamada)}</td>
          <td>${esc(v.nombreCliente)}</td>
          <td>${v.monto ?? ""}</td>
          <td>${esc(v.estado)}</td>
          <td>${esc(v.fechaRegistro)}</td>
        `;
        tbodyMis.appendChild(tr);
      }

      btnPrevMis.disabled = page <= 0;
      btnNextMis.disabled = !!res.last;

      if (!res.content?.length) {
        msgMis.className = "msg";
        msgMis.textContent = "Sin resultados";
        msgMis.hidden = false;
      }
    } catch (err) {
      msgMis.className = "msg err";
      msgMis.textContent = `${err.status || ""} ${err.message}`;
      msgMis.hidden = false;
    }
  }

  // cargar inicial mis ventas
  setTimeout(() => cargarMisVentas().catch(()=>{}), 200);

  btnBuscarMis.addEventListener("click", async () => {
    page = 0;
    await cargarMisVentas();
  });
  btnPrevMis.addEventListener("click", async () => {
    page = Math.max(0, page - 1);
    await cargarMisVentas();
  });
  btnNextMis.addEventListener("click", async () => {
    page += 1;
    await cargarMisVentas();
  });

  // ===== submit crear venta =====
  form.addEventListener("submit", async (e) => {
    e.preventDefault();
    msgCrear.hidden = true;

    // validación final por si acaso
    validateAllForm();
    if (btnCrear.disabled) {
      msgCrear.className = "msg err";
      msgCrear.textContent = "Completa todos los datos";
      msgCrear.hidden = false;
      return;
    }

    const fd = new FormData(form);
    const payload = Object.fromEntries(fd.entries());

    // normaliza dígitos
    payload.dniCliente = String(payload.dniCliente || "").replace(/\D/g, "").trim();
    payload.telefonoCliente = String(payload.telefonoCliente || "").replace(/\D/g, "").trim();
    payload.monto = Number(payload.monto);

    try {
      await api.crearVenta(payload);

      msgCrear.className = "msg ok";
      msgCrear.textContent = "Venta creada correctamente";
      msgCrear.hidden = false;

      form.reset();
      btnCrear.disabled = true;
      inputDni.classList.remove("valid", "invalid");
      inputTel.classList.remove("valid", "invalid");
      dniMsg.textContent = "";
      telMsg.textContent = "";

      page = 0;
      await cargarMisVentas();
    } catch (err) {
      msgCrear.className = "msg err";
      msgCrear.textContent = `${err.status || ""} ${err.message}`;
      msgCrear.hidden = false;
    }
  });

  // estado inicial del botón
  btnCrear.disabled = true;
}