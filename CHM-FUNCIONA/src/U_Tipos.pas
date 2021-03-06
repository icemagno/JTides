unit U_Tipos;
{Unit usada para novas defini��es de Tipos de Dados.}

interface

uses Controls;

{VARI�VEIS}

type PeqInteiroSs = Word; {Inteiro pequeno sem sinal. Valores: [0..65535] [2 bytes]}
type LongoInteiroSs = LongWord; {Inteiro longo sem sinal. Valores: [0..4294967295] [4 bytes]}
type MinInteiroSs = Byte; {Inteiro muito pequeno sem sinal. Valores: [0..255] [1 byte]}
type TCodEstacao = Word;
type TCodAnalise = LongWord;
type TArea = 0..2;
type TCodEquipEstacao = Word;
type TData = TDate;
type TDataHora = TDateTime;
type TNumEstacao = LongWord;
type TAmostragem = Byte;
type TCodEquipamento = Byte;
type TFiltroNm = 1..2;
type TCodComponentePac = Word;
type TAnoTabua = 1831..2080;
type TCodTabua = Word;
type TCodPorto = Word;

{REGISTROS}

type
  RegAltHor = record
    CodEstacao: integer;
    CodEquipamento: integer;
    Data: string[10];
    Alturas: array [1..24] of integer;
    //Ok: char;
  end;

type
  RegDiasTemp = record
    Data: string[10];
    QtdAlt: integer;
    QtdHorasCheias: integer;
  end;

type
  RegPeriodosTemp = record
    DataIni: string[10];
    DataFim: string[10];
    TotDias: integer;
    Situacao: integer;
  end;

type
  RegPeriodosConstantes = record
    Area: TArea;
    CodEstacao: TCodEstacao;
    CodAnalise: TCodAnalise;
    CodEquipEstacao: TCodEquipEstacao;
    DataIniPer: string[10];
    DataFimPer: string[10];
    PerPadrao: string[1];
    PerPadraoAnt: string[1];
    DataIniPerPadrao: string[10];
    DataFimPerPadrao: string[10];
    Z0: Real;
    NumComponentes: Byte;
    Origem: Byte;
    TaxaAmostragem: TAmostragem;
  end;

type
  RegFases = record
    Data: TDate;
    Hora: string[5];
    PorcentagemFase: Real;
  end;

type
  RegPeriodosAlturas = record
    Area: TArea;
    CodEquipEstacao: TCodEquipEstacao;
    CodEquipamento: TCodEquipamento;
    NomeEquipamento: string[80];
    TaxaAmostragem: TAmostragem;
    DataInicial: string[10];
    DataFinal: string[10];
    TotDias: LongoInteiroSs;
    TotAlturas: LongoInteiroSs;
  end;  

type
  RegFormatoCargaAlturas = record
    CodEstacao: array [1..6] of char;
    DataHora: array [1..19] of char;
    CodEquipamento: array [1..3] of char;
    Altura: array [1..5] of char;
    Origem: array [1..2] of char;
    LF: array [1..2] of char;
  end;

type
  RegGrafComparativo = record
    CodEstacao: integer;
    NumEstacao: integer;
    NomeEstacao: string[100];
    CodEquipamento: integer;
    CodAnaliseMares: integer;
    DataInicioPeriodoConstantes: string[10];
    DataFinalPeriodoConstantes: string[10];
    Z0: real;
  end;

type
  RegInclusaoAlturas = record
    CodEstacao: PeqInteiroSs;
    CodEquipEstacao: PeqInteiroSs;
    DataHora: string[16];
    Altura: PeqInteiroSs;
    Incluida: Boolean;
  end;

type
  RegGraficoComparativo = record
    {Tipo Gr�fico: 1 - Observa��o; 2 - Previs�o}
    TipoGrafico: MinInteiroSs;
    Area: MinInteiroSs;
    CodEstacao: PeqInteiroSs;
    CodEquipEstacao: PeqInteiroSs;
    CodAnalise: Integer;
    DataIniObs: string[10];
    DataFimObs: string[10];
    DataIniPrevisao: string[10];
    DataFimPrevisao: string[10];
    Z0: Real;
    Media: MinInteiroSs;
    Filtro: MinInteiroSs;
  end;

type
  RegConfigSistema = record
    Provider: string[30];
    PersistInfo: string[5];
    InitialCatalog: string[50];
    DataSource: string[50];
    Usuario: string[20];
    Senha: string[30];
    NomeLogin: string[20];
    CheckConnection: string[10];
    ProcPrepare: string[5];
    Translate: string[5];
    Packet: string[10];
    EncryptionData: string[5];
    Collation: string[5];
    SplashTime: string[5];
    SqlDateFormat: string[10];
    Ftp: string[3];
    DiretorioSistema: string[150];
    VersaoSistema: string[10];
    SeparadorDecimal: Char;
    FormatoData: string[10];
    PermissoesUsuario: array [1..10] of Boolean;
    DirAlturasTemp: string[150];
    DirAnalisesTemp: string[150];
    DirPrevisoesTemp: string[150];
    DirAlturas: string[150];
    DirAnalises: string[150];
    DirPrevisoes: string[150];
    DirTabuas: string[150];
    DirGraficos: string[150];
    DirLogs: string[150];
    DirTemp: string[150];
  end;

type
  RegPrevIntercambio = record
    CodEstacao: PeqInteiroSs;
    NumEstacao: LongoInteiroSs;
    CodAnalise: LongoInteiroSs;
    Z0: Real;
    DataInicial: TDate;
    DataFinal: TDate;
    TipoRelatorio: MinInteiroSs;
    TipoPrevisao: MinInteiroSs;
  end;

type
  RegOrigemDado = record
    CodOrigemObs: PeqInteiroSs;
    Descricao: string[100];
    TipoOrigem: MinInteiroSs;
  end;

type
  RegPeriodosPrev = record
    DataInicial: string[10];
    DataFinal: string[10];
  end;

type
  RegAltura = record
    DataHora: TDateTime;
    Altura: Real;
  end;

{VETORES}

type VetCalc = array [1..10] of string[16];
type VetOrigemDado = array of RegOrigemDado;
type VetCalc2 = array [1..6] of string[16];

implementation

end.
