unit U_ClassePrevisaoSis;

interface

uses
  SysUtils, Windows, Controls, BNDO, DB, ADODB, U_Dll,
  U_ClasseRotinasGenericas, U_Tipos;

type
  TClassePrevisaoSis = class
  private
    CRotGen: TClasseRotinasGenericas;
  public
    constructor create;
    procedure previsaoColunas(vDataInicial, vDataFinal: TDateTime;
      Z0: Real; ArquivoConst, ArquivoPrev: string);
    procedure previsaoColunasGeral(DataIni, DataFim: TDate; Arquivo: string;
      Z0: Real; TipoSaida: MinInteiroSs; Cabecalho, DeletaConst: Boolean);
  end;

implementation

constructor TClassePrevisaoSis.create;
begin
//
end;

procedure TClassePrevisaoSis.previsaoColunas(vDataInicial, vDataFinal: TDateTime;
  Z0: Real; ArquivoConst, ArquivoPrev: string);
{
  1 - Previsão Horária;
  2 - Previsão Máximas e Mínimas.
}
var
  ArqConstDll, ArqPrevDll: array [1..150] of Char;
  Tipo, Op, Sim: array [1..1] of Char;
  Di, Mi, Df, Mf: array [1..2] of Char;
  Ai, Af: array [1..4] of Char;
  Nivel: array [1..10] of Char;
  TArqPrev, TArqPrevSaida: TextFile;
  ArqConst, ArqPrev, ArqPrevAux, ArqAux: string;
  AnoIni, AnoFim, AnoAux: string[4];
  VetPeriodosPrev: array of RegPeriodosPrev;
  Indice, I, DiaAux: Shortint;
  DataPrev: string[10];
  HoraPrev: string[6];
  AlturaPrev: string[5];
begin
  AnoIni:= FormatDateTime('yyyy', vDataInicial);
  AnoAux:= FormatDateTime('yyyy', vDataInicial);
  AnoFim:= FormatDateTime('yyyy', vDataFinal);
  Indice:= 1;
  SetLength(VetPeriodosPrev, Indice);
  VetPeriodosPrev[Indice-1].DataInicial:= '01/' + FormatDateTime('mm/yyyy', vDataInicial);
  while (AnoAux <> AnoFim) do
    begin
      VetPeriodosPrev[Indice-1].DataFinal:= '31/12/' + AnoAux;
      Indice:= Indice + 1;
      SetLength(VetPeriodosPrev, Indice);
      AnoAux:= IntToStr(StrToInt(AnoAux) + 1);
      VetPeriodosPrev[Indice-1].DataInicial:= '01/01/' + AnoAux;
    end;
  DiaAux:= 31;
  VetPeriodosPrev[Indice-1].DataFinal:= FormatFloat('00', DiaAux) + '/' + FormatDateTime('mm/yyyy', vDataFinal);
  while not (CRotGen.ValidaDataString(VetPeriodosPrev[Indice-1].DataFinal)) do
    begin
      DiaAux:= DiaAux - 1;
      VetPeriodosPrev[Indice-1].DataFinal:= FormatFloat('00', DiaAux) + '/' + FormatDateTime('mm/yyyy', vDataFinal);
    end;
  ArqConst:= ArquivoConst;
  BNDO.strtochar(ArqConstDll, ArqConst);
  BNDO.strtochar(Nivel, FormatFloat('0000000.00', Z0));
  BNDO.strtochar(Op, 'N');
  BNDO.strtochar(Sim, 'S');
  ArqPrev:= ArquivoPrev;
  ArqPrevAux:= ArquivoPrev + '.tmp';
  if (FileExists(ArqPrev)) then
    SysUtils.DeleteFile(ArqPrev);
  if (FileExists(ArqPrevAux)) then
    SysUtils.DeleteFile(ArqPrevAux);
  for I:= 1 to Indice do
    begin
      BNDO.strtochar(Di, Copy(VetPeriodosPrev[I-1].DataInicial, 1, 2));
      BNDO.strtochar(Mi, Copy(VetPeriodosPrev[I-1].DataInicial, 4, 2));
      BNDO.strtochar(Ai, Copy(VetPeriodosPrev[I-1].DataInicial, 7, 4));
      BNDO.strtochar(Df, Copy(VetPeriodosPrev[I-1].DataFinal, 1, 2));
      BNDO.strtochar(Mf, Copy(VetPeriodosPrev[I-1].DataFinal, 4, 2));
      BNDO.strtochar(Af, Copy(VetPeriodosPrev[I-1].DataFinal, 7, 4));
      ArqAux:= 'PREV' + IntToStr(I) + '.tmp';
      if (FileExists(ArqAux)) then
        SysUtils.DeleteFile(ArqAux);
      BNDO.strtochar(Tipo, '1');
      BNDO.strtochar(ArqPrevDll, ArqAux);
      U_Dll.PREVISAO_ALTURAS_EXCEL(ArqConstDll, ArqPrevDll, Tipo, Di, Mi, Ai, Df, Mf, Af, Nivel, Op);
      CRotGen.ConcatenaArquivos(ArqPrevAux, ArqPrevDll, 1, True);
      //U_Dll.PREVISAO_MAXMIN_EXCEL(ArqConstDll, ArqPrevDll, Tipo, Di, Mi, Ai, Df, Mf, Af, Nivel, Op, Sim);
      //CRotGen.ConcatenaArquivos(ArqPrevAux, ArqPrevDll, 2, True);
    end;
  AssignFile(TArqPrevSaida, ArqPrev);
  AssignFile(TArqPrev, ArqPrevAux);
  if (FileExists(ArqPrev)) then
    Append(TArqPrevSaida)
  else
    Rewrite(TArqPrevSaida);
  Reset(TArqPrev);
  while not(Eof(TArqPrev)) do
    begin
      Readln(TArqPrev, DataPrev, AlturaPrev, HoraPrev);
      //Readln(TArqPrev, DataPrev, HoraPrev, AlturaPrev);
      if ((StrToDate(Trim(DataPrev)) >= vDataInicial) and (StrToDate(Trim(DataPrev)) <= vDataFinal)) then
        Writeln(TArqPrevSaida, Trim(DataPrev) + ' ' + Trim(HoraPrev) + AlturaPrev);
    end;
  CloseFile(TArqPrevSaida);
  CloseFile(TArqPrev);
  SysUtils.DeleteFile(ArqPrevAux);
end;

procedure TClassePrevisaoSis.previsaoColunasGeral(DataIni, DataFim: TDate; Arquivo: string;
  Z0: Real; TipoSaida: MinInteiroSs; Cabecalho, DeletaConst: Boolean);
{
  1 - Previsão Horária;
  2 - Previsão Máximas e Mínimas.
}
var
  ArqConstDll, ArqPrevDll: array [1..150] of Char;
  Tipo, Op, Sim: array [1..1] of Char;
  Di, Mi, Df, Mf: array [1..2] of Char;
  Ai, Af: array [1..4] of Char;
  Nivel: array [1..10] of Char;
  TArqPrev, TArqPrevSaida: TextFile;
  ArqConst, ArqPrev, ArqPrevAux: string;
  NumEstacao, DataIniArq, DataFimArq: string;
  AnoIni, AnoFim, AnoAux: string[4];
  VetPeriodosPrev: array of RegPeriodosPrev;
  Indice, I, DiaAux: Shortint;
  DataPrev: string[10];
  HoraPrev: string[6];
  AlturaPrev: string[5];
  CRotinasGenericas: TClasseRotinasGenericas;
begin
  {CRotinasGenericas:= TClasseRotinasGenericas.Create;
  AnoIni:= FormatDateTime('yyyy', DataIni);
  AnoAux:= FormatDateTime('yyyy', DataIni);
  AnoFim:= FormatDateTime('yyyy', DataFim);
  Indice:= 1;
  SetLength(VetPeriodosPrev, Indice);
  VetPeriodosPrev[Indice-1].DataInicial:= '01/' + FormatDateTime('mm/yyyy', DataIni);
  while (AnoAux <> AnoFim) do
    begin
      VetPeriodosPrev[Indice-1].DataFinal:= '31/12/' + AnoAux;
      Indice:= Indice + 1;
      SetLength(VetPeriodosPrev, Indice);
      AnoAux:= IntToStr(StrToInt(AnoAux) + 1);
      VetPeriodosPrev[Indice-1].DataInicial:= '01/01/' + AnoAux;
    end;
  DiaAux:= 31;
  VetPeriodosPrev[Indice-1].DataFinal:= FormatFloat('00', DiaAux) + '/' + FormatDateTime('mm/yyyy', DataFim);
  while not (CRotinasGenericas.ValidaDataString(VetPeriodosPrev[Indice-1].DataFinal)) do
    begin
      DiaAux:= DiaAux - 1;
      VetPeriodosPrev[Indice-1].DataFinal:= FormatFloat('00', DiaAux) + '/' + FormatDateTime('mm/yyyy', DataFim);
    end;
  NumEstacao:= FormatFloat('00000', CRotinasGenericas.RetornaNumeroEstacao(DtMod.BD, CodEstacao));
  DataIniArq:= BNDO.converte_data_arq(FormatDateTime('dd/mm/yyyy', DataIni));
  DataFimArq:= BNDO.converte_data_arq(FormatDateTime('dd/mm/yyyy', DataFim));
  ArqConst:= ExtractFilePath(Arquivo) + NumEstacao + DataIniArq + DataFimArq + 'CH.txt';
  if (Origem in[2,3]) then
    geraArquivoConstantesPacDll(ArqConst)
  else
    geraArquivoConstantesSisDll(ArqConst);
  BNDO.strtochar(ArqConstDll, ArqConst);
  BNDO.strtochar(Nivel, FormatFloat('0000000.00', Z0));
  BNDO.strtochar(Op, 'N');
  BNDO.strtochar(Sim, 'S');
  ArqPrev:= Arquivo + '.tmp';
  if (FileExists(Arquivo)) then
    SysUtils.DeleteFile(Arquivo);
  if (FileExists(ArqPrev)) then
    SysUtils.DeleteFile(ArqPrev);
  if (Cabecalho) then
    begin
      //GeraCabecalhoPrevisaoHoraria(Conexao, Area, CodAnalise, DataIni, DataFim, ArqPrev);
    end;
  for I:= 1 to Indice do
    begin
      BNDO.strtochar(Di, Copy(VetPeriodosPrev[I-1].DataInicial, 1, 2));
      BNDO.strtochar(Mi, Copy(VetPeriodosPrev[I-1].DataInicial, 4, 2));
      BNDO.strtochar(Ai, Copy(VetPeriodosPrev[I-1].DataInicial, 7, 4));
      BNDO.strtochar(Df, Copy(VetPeriodosPrev[I-1].DataFinal, 1, 2));
      BNDO.strtochar(Mf, Copy(VetPeriodosPrev[I-1].DataFinal, 4, 2));
      BNDO.strtochar(Af, Copy(VetPeriodosPrev[I-1].DataFinal, 7, 4));
      ArqPrevAux:= ExtractFilePath(Arquivo) + NumEstacao + DataIniArq + DataFimArq + IntToStr(I) + '.tmp';
      if (FileExists(ArqPrevAux)) then
        SysUtils.DeleteFile(ArqPrevAux);
      case (TipoSaida) of
        1: begin
             BNDO.strtochar(Tipo, '1');
             BNDO.strtochar(ArqPrevDll, ArqPrevAux);
             U_Dll.PREVISAO_ALTURAS_EXCEL(ArqConstDll, ArqPrevDll, Tipo, Di, Mi, Ai, Df, Mf, Af, Nivel, Op);
             CRotinasGenericas.ConcatenaArquivos(ArqPrev, ArqPrevDll, 1, True);
           end;
        2: begin
             BNDO.strtochar(Tipo, '2');
             BNDO.strtochar(ArqPrevDll, ArqPrevAux);
             U_Dll.PREVISAO_MAXMIN_EXCEL(ArqConstDll, ArqPrevDll, Tipo, Di, Mi, Ai, Df, Mf, Af, Nivel, Op, Sim);
             CRotinasGenericas.ConcatenaArquivos(ArqPrev, ArqPrevDll, 2, True);
           end;
      end;
    end;
  if (DeletaConst) then
    SysUtils.DeleteFile(ArqConst);
  FreeAndNil(CRotinasGenericas);
  AssignFile(TArqPrevSaida, Arquivo);
  AssignFile(TArqPrev, ArqPrev);
  if (FileExists(Arquivo)) then
    Append(TArqPrevSaida)
  else
    Rewrite(TArqPrevSaida);
  Reset(TArqPrev);
  while not(Eof(TArqPrev)) do
    begin
      case (TipoSaida) of
        1: Readln(TArqPrev, DataPrev, AlturaPrev, HoraPrev);
        2: Readln(TArqPrev, DataPrev, HoraPrev, AlturaPrev);
      end;
      if ((StrToDate(Trim(DataPrev)) >= DataIni) and (StrToDate(Trim(DataPrev)) <= DataFim)) then
        Writeln(TArqPrevSaida, Trim(DataPrev) + ' ' + Trim(HoraPrev) + AlturaPrev);
    end;
  CloseFile(TArqPrevSaida);
  CloseFile(TArqPrev);
  SysUtils.DeleteFile(ArqPrev);}
end;

end.
