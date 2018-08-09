unit U_ClasseDirecionador;

interface

uses U_ClasseAnaliseHarmonicaSis, U_ClassePrevisaoSis;

type
  TDirecionador = class
  private
  public
    procedure analiseHarmonica(ArqAlt, ArqAh, ArqConst: string);
    procedure previsao(ArqConst, ArqPrev: string; DataInicial,
      DataFinal: TDateTime; Z0: Real);
  end;

implementation

procedure TDirecionador.analiseHarmonica(ArqAlt, ArqAh, ArqConst: string);
var
  AH: TClasseAnaliseHarmonicaSis;
begin
  AH:= TClasseAnaliseHarmonicaSis.create;
  AH.analiseHarmonica(tAh, ArqAlt, ArqAh, ArqConst);
end;

procedure TDirecionador.previsao(ArqConst, ArqPrev: string; DataInicial,
  DataFinal: TDateTime; Z0: Real);
var
  Prev: TClassePrevisaoSis;
begin
  Prev:= TClassePrevisaoSis.create;
  Prev.previsaoColunas(DataInicial, DataFinal, Z0, ArqConst, ArqPrev);
end;

end.
