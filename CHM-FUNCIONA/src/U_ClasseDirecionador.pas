unit U_ClasseDirecionador;

interface

uses U_ClasseAnaliseHarmonicaSis, U_ClassePrevisaoSis, U_Magno, SysUtils;

type
  TDirecionador = class
  private
  public
    procedure CarregaFasesLua( Ano: Word; Fuso: Smallint; ArquivoFases: string);
    procedure analiseHarmonica(ArqAlt, ArqAh, ArqConst: string);
    procedure previsao(ArqConst, ArqPrev: string; DataInicial, DataFinal: TDateTime; Z0: Real; TipoSaida : Integer);
    procedure GeraArqAlturasDll(CodEstacao : Word; CodEqpto : Word; DataIni, DataFim, ArqConst: string);
    procedure GeraArqConstantesDll( CodEstacao: Word; CodAnalise: integer; Arquivo: string  );
    procedure teste();
  end;

implementation

// Cod. Estacao, Cod Equipamento, Dta inicial, Dta final, Arquivo destino
procedure TDirecionador.GeraArqAlturasDll( CodEstacao : Word; CodEqpto : Word; DataIni, DataFim, ArqConst: string );
var MG : TMagno;
begin
  MG := TMagno.Create();
  MG.GeraArqAlturasDll( CodEstacao, CodEqpto, DataIni, DataFim, ArqConst );
end;


// Cod Estacao, Cod Equipamento, Arquivo destino
procedure TDirecionador.GeraArqConstantesDll( CodEstacao: Word; CodAnalise: integer; Arquivo: string  );
var MG : TMagno;
begin
  MG := TMagno.Create();
  MG.GeraArqConstantesDll( CodEstacao, CodAnalise, Arquivo );
end;

procedure TDirecionador.analiseHarmonica(ArqAlt, ArqAh, ArqConst: string);
var
  AH: TClasseAnaliseHarmonicaSis;
begin
  AH:= TClasseAnaliseHarmonicaSis.create;
  AH.analiseHarmonica(tAh, ArqAlt, ArqAh, ArqConst);
end;


procedure TDirecionador.CarregaFasesLua( Ano: Word; Fuso: Smallint; ArquivoFases: string);
var MG : TMagno;
begin
  MG := TMagno.Create();
  MG.CarregaFasesLua(Ano,Fuso,ArquivoFases);
end;


procedure TDirecionador.teste();
var MG : TMagno;
begin
  MG := TMagno.Create();

  MG.geraTabua( 256, 50140, 'c:\magno\anhar\', 2018, 'C:\Magno\anhar\constantes-tabua.txt');


  //MG.CarregaFasesLua(2018,3,'C:\Magno\anhar\faseslua.txt');

  //MG.previsaoColunas(256,636,EncodeDate(2018,01,01),EncodeDate(2020,12,31),'c:\magno\anhar\',1,'C:\Magno\anhar\constantes.txt');
  //MG.previsaoColunas(256,636,EncodeDate(2018,01,01),EncodeDate(2020,12,31),'c:\magno\anhar\',2,'C:\Magno\anhar\constantes.txt');

end;


procedure TDirecionador.previsao(ArqConst, ArqPrev: string; DataInicial,
  DataFinal: TDateTime; Z0: Real; TipoSaida : Integer);
var
  Prev: TClassePrevisaoSis;
begin
  Prev:= TClassePrevisaoSis.create;
  Prev.previsaoColunas(DataInicial, DataFinal, Z0, ArqConst, ArqPrev, TipoSaida);
end;

end.
