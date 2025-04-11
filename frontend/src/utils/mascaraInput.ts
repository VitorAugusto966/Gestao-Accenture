export function limparMascara(str: string) {
  return str.replace(/\D/g, '')
}

export function mascararCep(cep: string): string {
  let valor = cep.replace(/\D/g, '')
  if (valor.length > 8) valor = valor.slice(0, 8)
  if (valor.length > 5) valor = valor.slice(0, 5) + '-' + valor.slice(5)
  return valor
}

export function mascararDocumento(documento: string, tipo: string): string {
  let valor = documento.replace(/\D/g, '')

  if (tipo === 'PJ') {
    if (valor.length > 14) valor = valor.slice(0, 14)
    valor = valor
      .replace(/^(\d{2})(\d)/, '$1.$2')
      .replace(/^(\d{2})\.(\d{3})(\d)/, '$1.$2.$3')
      .replace(/\.(\d{3})(\d)/, '.$1/$2')
      .replace(/(\d{4})(\d)/, '$1-$2')
    return valor
  } else {
    if (valor.length > 11) valor = valor.slice(0, 11)
    valor = valor
      .replace(/^(\d{3})(\d)/, '$1.$2')
      .replace(/^(\d{3})\.(\d{3})(\d)/, '$1.$2.$3')
      .replace(/\.(\d{3})(\d)/, '.$1-$2')
    return valor
  }
}

export function mascararRg(rg: string): string {
  let valor = rg.replace(/\D/g, '')
  if (valor.length > 9) valor = valor.slice(0, 9)
  valor = valor.replace(/^(\d{2})(\d{3})(\d{3})(\d)$/, '$1.$2.$3-$4')
  return valor
}

export function aplicarMascara(fornecedor: any): string {
  return mascararDocumento(fornecedor.documento, fornecedor.tipo)
}

export function formatarCNPJ(cnpj: string): string {
  const valor = cnpj.replace(/\D/g, '')
  if (valor.length !== 14) return cnpj
  return valor.replace(/^(\d{2})(\d{3})(\d{3})(\d{4})(\d{2})$/, '$1.$2.$3/$4-$5')
}
